package silverpotion.userserver.payment.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.payment.domain.CashItem;
import silverpotion.userserver.payment.dtos.CashItemPrepareReqDto;
import silverpotion.userserver.payment.dtos.CashItemPrepareResDto;
import silverpotion.userserver.payment.dtos.CashItemRefundDto;
import silverpotion.userserver.payment.dtos.CashItemVerifyRequest;
import silverpotion.userserver.payment.repository.CashItemRepository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CashItemService {
    private final CashItemRepository cashItemRepository;
    private final UserRepository userRepository;
    private final IamportClient iamportClient;
    @Qualifier("pay")
    private final RedisTemplate<String,String> redisTemplate;


    public CashItemService(CashItemRepository cashItemRepository, UserRepository userRepository, IamportClient iamportClient, @Qualifier("pay") RedisTemplate<String, String> redisTemplate) {
        this.cashItemRepository = cashItemRepository;
        this.userRepository = userRepository;
        this.iamportClient = iamportClient;
        this.redisTemplate = redisTemplate;
    }

//  1.사전검증
    public CashItemPrepareResDto preparePayment(String loginId, CashItemPrepareReqDto dto){
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        //힐링포션 구매 수량
        int quantity = dto.getQuantity();

        if(quantity<=0){
            throw new IllegalArgumentException("유효하지 않은 수량입니다");
        }
        int totalAmount = quantity * 1000;//총 가격 =힐링포션 개수 * 1000원
        String merchantUid = String.valueOf(UUID.randomUUID()); // 결제 건을 고유하게 식별하기 위한 고유 주문번호
       try {
           //포트원 라이브러리에서 제공하는 사전검증요청객체(PrepareData)는 결제건 고유의 ID와 BigDecimal타입의 결제금액을 인자로 요구한다.
           PrepareData prepareData = new PrepareData(merchantUid, BigDecimal.valueOf(totalAmount));
           //포트원의 사전검증API를 호출. 이걸 호출하면 포트원 서버에 merchant_uid,amount가 등록되고, 이후 프론트에서 결제 실행시 금액이 다르면 결제를 막아준다.
           iamportClient.postPrepare(prepareData);
           //레디스에도 결제번호와 금액을 저장. TTL설정을 걸어 10분뒤 삭제되는 캐시형태로 저장(결제 후 비교를 위해)
           redisTemplate.opsForValue().set(merchantUid,String.valueOf(totalAmount),10, TimeUnit.MINUTES);

       } catch (IOException | IamportResponseException e){
           throw new RuntimeException("포트원 사전검증 등록 실패",e);
       }
        return CashItemPrepareResDto.builder().merchant_uid(merchantUid).amount(totalAmount).quantity(quantity).build();
    }

//   2. 사후검증
    public void afterSuccessPayment(String loginId, CashItemVerifyRequest dto) {
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));

        try {
            //1.포트원 서버에서 결제 정보 조회
            //paymentByImpUid는 imp_uid로 포트원에 등록된 결제정보를 요청하는 메서드
            //imp_uid는 포트원 서버가 생성하는 것으로 실제 결제된 건을 고유하게 식별하기위한 번호다
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(dto.getImp_uid());
            Payment iamportPayment = response.getResponse(); //결제정보
            //2. 결제 상태 확인
            if (!"paid".equals(iamportPayment.getStatus())) {
                throw new IllegalArgumentException("결제가 완료되지 않았습니다");
            }
            //3. 결제 금액 확인
            int paidAmount = iamportPayment.getAmount().intValue(); //포트원 서버에서 받아온 결제금액
            int count = paidAmount /1000; //갯수
            String redisKey = dto.getMerchant_uid();
            String expectedAmountStr = redisTemplate.opsForValue().get(redisKey); //사전검증 때 레디스에 저장해놓았던 결제금액
            if(expectedAmountStr == null){
                throw new IllegalArgumentException("사전검증 정보가 만료되거나 없습니다");
            }

            if(paidAmount !=  Integer.parseInt(expectedAmountStr)){
                throw new IllegalArgumentException("결제 금액 불일치 - 위변조 의심");
            }

            //4. 저장
            CashItem cashItem = CashItem.builder()
                    .impUid(dto.getImp_uid())
                    .merchantUid(dto.getMerchant_uid())
                    .user(user)
                    .amount(paidAmount)
                    .status(iamportPayment.getStatus())
                    .quantity(count)
                    .build();
            cashItemRepository.save(cashItem);

            user.getMyPaymentList().add(cashItem);// 해당 사용자의 결제 내역에 추가
            user.updateMyHealingPotion(count); //사용자의 힐링포션 갯수에 구매한 갯수만큼 추가

        } catch(IamportResponseException | IOException e){
            throw new RuntimeException("결제 검증 중 오류 발생",e);
        }
    }

//    3. 환불
    public void refundHealingPotion(String loginId, CashItemRefundDto dto){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        System.out.println(dto.getImpUid());
       try {
           //CancelData 포트원에 결제 취소요청을 보낼때 필요한 데이터 객체로 결제번호를 인자값으로 가진다 true는 전액환불여부
           CancelData cancelData = new CancelData(dto.getImpUid(), true);
           cancelData.setReason(dto.getReason());//취소사유
           IamportResponse<Payment> cancelResponse = iamportClient.cancelPaymentByImpUid(cancelData);

           if(!"cancelled".equals(cancelResponse.getResponse().getStatus())){
               throw new IllegalArgumentException("결제 취소 실패");
           }

        CashItem cashItem =  cashItemRepository.findByImpUid(dto.getImpUid()).orElseThrow(()->new EntityNotFoundException("없는 결제건입니다"));
           System.out.println(cashItem.getImpUid());
        cashItem.changePaymentStatus(); //취소상태로 변경

       } catch (IamportResponseException | IOException e){
           throw new RuntimeException("결제 취소 중 오류 발생",e);
       }
       }



}
