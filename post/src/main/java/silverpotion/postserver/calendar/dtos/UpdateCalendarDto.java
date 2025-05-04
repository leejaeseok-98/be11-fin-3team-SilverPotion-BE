package silverpotion.postserver.calendar.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdateCalendarDto {
    private Long id; //캘린더 id
    private String title;
    private String description;
    private String place;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean allDay;
}
