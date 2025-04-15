package silverpotion.userserver.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// 결제 사전 검증시 프론트로부터 전달받는 dto
public class CashItemPrepareReqDto {
    //구매할 힐링포션 갯수
    private int quantity;

}
