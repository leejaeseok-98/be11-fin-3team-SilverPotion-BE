package silverpotion.userserver.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PlanListResDto {
    private Long id;
    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
