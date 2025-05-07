package silverpotion.postserver.calendar.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.postserver.calendar.domain.Calendar;

import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CalendarDetailResDto {
    private String title;
    private String description;
    private String place;
    private String start;
    private String end;

    public static CalendarDetailResDto detailResDto(Calendar calendar){
        //yyy-mm--dd hh:mm 형식으로 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return CalendarDetailResDto.builder()
                .title(calendar.getTitle())
                .description(calendar.getDescription())
                .place(calendar.getPlace())
                .start(calendar.getStart().format(formatter))
                .end(calendar.getEnd().format(formatter))
                .build();
    }
}
