package silverpotion.userserver.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true) //없는필드 자동무시
public class GoogleProfileDto {
    private String sub;
    private String email;
    private String picture;
}
