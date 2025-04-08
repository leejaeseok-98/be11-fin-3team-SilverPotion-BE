package silverpotion.userserver.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.plan.domain.Plan;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanCreateDto {
    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public Plan toEntity(User user){
        return Plan.builder()
                .title(this.title)
                .content(this.content)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .user(user)
                .userLoginId(user.getLoginId())
                .build();
    }
}
