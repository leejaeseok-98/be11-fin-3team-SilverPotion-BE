package silverpotion.userserver.payment.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.payment.dtos.CashItemOfPaymentListDto;
import silverpotion.userserver.user.domain.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class CashItem extends BaseTimeEntity {
//CashItem 은 결제내역임
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //포트원이 발급한 결제건 고유번호
    private String impUid;
    //프론트에서 생성한 주문 고유번호
    private String merchantUid;
    //결제한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //결제 금액
    private int amount;
    //paid, cancelled(포트원서버로부터 받는 값임)
    private String status;
    //우리는 실버포션이라는 아이템 하나 밖에 없으니까 기본값
    @Builder.Default
    private String itemName = "silverPotion";
    //구매 개수
    private int quantity;


//    상태 업데이트 환불로 메서드
    public void changePaymentStatus(){
        this.status = "cancelled";
    }


    public CashItemOfPaymentListDto ListDtoFromEntity(){
        return CashItemOfPaymentListDto.builder()
                .itemName(this.itemName)
                .amount(this.amount)
                .quantity(this.quantity)
                .impUid(this.impUid)
                .status(this.status)
                .build();
    }





}
