package silverpotion.postserver.calendar.service;

import org.springframework.stereotype.Service;
import silverpotion.postserver.calendar.domain.Calendar;
import silverpotion.postserver.calendar.dtos.CalendarCreateResDto;
import silverpotion.postserver.calendar.repository.CalendarRepository;
import silverpotion.postserver.meeting.domain.Meeting;
import silverpotion.postserver.post.feignClient.UserClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CalendarService {
    private final CalendarRepository calendarRepository;
    private final UserClient userClient;

    public CalendarService(CalendarRepository calendarRepository, UserClient userClient) {
        this.calendarRepository = calendarRepository;
        this.userClient = userClient;
    }

    // 일정 등록
    public void createCalendar(String loginId, CalendarCreateResDto dto){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Calendar calendar = Calendar.builder()
                .userId(userId)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .start(dto.getStart())
                .place(dto.getPlace())
                .end(dto.getEnd())
                .allDay(dto.isAllDay())
                .source("사용자입력")
                .build();
        calendarRepository.save(calendar);
    }

    //정모 참석 시 자동 일정 등록
    public void registerMeetingEvent(Meeting meeting, String loginId){
        LocalDateTime start = LocalDateTime.of(meeting.getMeetingDate(),meeting.getMeetingTime());
        LocalDateTime end = start.plusHours(4);

        
    }

    //일정 조회
    public List<Calendar> getCalendar(String loginId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        return calendarRepository.findByUserId(userId);
    }
}
