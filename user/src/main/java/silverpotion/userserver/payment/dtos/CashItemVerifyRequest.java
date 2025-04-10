package silverpotion.userserver.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CashItemVerifyRequest {
    private String imp_uid;
    private String merchant_uid;


}
