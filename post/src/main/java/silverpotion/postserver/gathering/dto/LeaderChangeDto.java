package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LeaderChangeDto {
    private Long userId; // 새로운 모임장 ID
}
