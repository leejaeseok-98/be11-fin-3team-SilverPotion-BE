package silverpotion.userserver.plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlanUpdateDto {
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
