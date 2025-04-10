package silverpotion.userserver.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
//결제사전 검증시 백엔드에서 프론트에 전달할 dto
public class CashItemPrepareResDto {
        private String merchant_uid;
        private  int amount;
        @Builder.Default
        private String name ="힐링포션";
        private int quantity;
}
