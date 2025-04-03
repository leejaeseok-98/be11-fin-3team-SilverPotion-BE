package silverpotion.postserver.meeting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AttendeeDto {
    private Long userId;
    private String nickname;
    private String profileImage;
}
