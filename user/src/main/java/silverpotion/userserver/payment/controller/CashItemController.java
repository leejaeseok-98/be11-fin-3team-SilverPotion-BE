package silverpotion.userserver.payment.controller;

import com.google.api.Http;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.payment.dtos.CashItemPrepareReqDto;
import silverpotion.userserver.payment.dtos.CashItemPrepareResDto;
import silverpotion.userserver.payment.dtos.CashItemRefundDto;
import silverpotion.userserver.payment.dtos.CashItemVerifyRequest;
import silverpotion.userserver.payment.service.CashItemService;

@RestController
@RequestMapping("silverpotion/payment")
public class CashItemController {

        private final CashItemService cashItemService;

    public CashItemController(CashItemService cashItemService) {
        this.cashItemService = cashItemService;

    }

//    1. 결제들어가기전 사전검증
  @PostMapping("/prepare")
    public ResponseEntity<?> preparePayment(@RequestHeader("X-User-LoginId")String loginId, @RequestBody CashItemPrepareReqDto dto){
                    CashItemPrepareResDto resDto = cashItemService.preparePayment(loginId,dto);
    return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"sucess",resDto),HttpStatus.OK);
  }

//  2. 결제 성공 후 사후검증(사전검증이 완료되면 엔티티에 저장)
    @PostMapping("/afterPayment")
     public ResponseEntity<?> afterSuccessPayment(@RequestHeader("X-User-LoginId")String loginId, @RequestBody CashItemVerifyRequest request){
                        cashItemService.afterSuccessPayment(loginId,request);
                        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success","success"),HttpStatus.OK);
    }

//  3.환불처리
    @PostMapping("/refund")
      public ResponseEntity<?> refundHealingPotion(@RequestHeader("X-User-LoginId")String loginId, @RequestBody CashItemRefundDto dto){
                  cashItemService.refundHealingPotion(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "refund success","success"),HttpStatus.OK);
    }



}
