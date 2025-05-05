package silverpotion.postserver.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalendarCreateResDto {
    private String title;
    private String description;
    private String place;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean allDay;
}
