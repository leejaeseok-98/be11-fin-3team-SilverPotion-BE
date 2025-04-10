package silverpotion.userserver.payment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CashItemRefundDto {
    //결제번호
    private String impUid;
    //이유
    private String reason;

}
