package silverpotion.userserver.payment.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// 화면에 결제내역 보이기 위해 프론트에 전달할용도의 dto
public class CashItemOfPaymentListDto {
    private String itemName; //힐링포션
    private String status; //결제상태
    private int amount; //결제금액
    private int quantity; //구매개수
    private String impUid; //결제번호

}
