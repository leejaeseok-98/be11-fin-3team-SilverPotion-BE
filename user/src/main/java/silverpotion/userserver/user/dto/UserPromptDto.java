package silverpotion.userserver.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.healthData.domain.HealthData;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserPromptDto {

        private HealthData healthData;
        private String prompt;

}
