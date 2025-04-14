package silverpotion.userserver.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AutoCodeDto {
    private String phoneNumber;
    private String autoCode;
}
