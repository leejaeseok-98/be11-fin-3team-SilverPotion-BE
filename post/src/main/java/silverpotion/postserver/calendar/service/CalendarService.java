package silverpotion.postserver.calendar.service;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.sql.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.postserver.calendar.domain.Calendar;
import silverpotion.postserver.calendar.dtos.CalendarCreateResDto;
import silverpotion.postserver.calendar.dtos.CalendarDetailResDto;
import silverpotion.postserver.calendar.dtos.UpdateCalendarDto;
import silverpotion.postserver.calendar.repository.CalendarRepository;
import silverpotion.postserver.meeting.domain.Meeting;
import silverpotion.postserver.post.feignClient.UserClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
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
        Long userId = userClient.getUserIdByLoginId(loginId);
        LocalDateTime start = LocalDateTime.of(meeting.getMeetingDate(),meeting.getMeetingTime());
        LocalDateTime end = start.plusHours(4);

        Calendar calendar = Calendar.builder()
                .userId(userId)
                .title("[정모]" + meeting.getName())
                .start(start)
                .end(end)
                .place(meeting.getPlace())
                .description("")//설명은 어떻게 자동으로 넣지?
                .allDay(false) //정모기간이 1박2일 이상일 경우는??
                .source("정모")
                .build();
        calendarRepository.save(calendar);
    }

    //일정 조회
    public List<Calendar> getCalendar(String loginId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        return calendarRepository.findByUserId(userId);
    }

    //일정 상세조회
    public CalendarDetailResDto getDetail(Long calendarId){
        Calendar calendar = calendarRepository.findById(calendarId).orElseThrow(()-> new EntityNotFoundException("등록된 일정이 없습니다"));
        return CalendarDetailResDto.detailResDto(calendar);
    }

    //일정 수정
    public void updateCalendar(String loginId, UpdateCalendarDto dto){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Calendar calendar = calendarRepository.findById(dto.getId()).orElseThrow(()-> new EntityNotFoundException("등록된 일정이 없습니다."));

        if (!(calendar.getUserId().equals(userId))){
            throw new IllegalArgumentException("본인의 일정만 수정할 수 있습니다.");
        }
        if ("정모".equals(calendar.getSource())){
            throw new IllegalArgumentException("정모 일정은 수정할 수 없습니다.");
        }

        calendar.updateCalendar(dto);
        calendarRepository.save(calendar);

    }

    //일정 삭제
    public void deleteCalendar(Long calendarId, String loginId){
        Long userId = userClient.getUserIdByLoginId(loginId);
        Calendar calendar = calendarRepository.findById(calendarId).orElseThrow(()-> new EntityNotFoundException("일정을 찾을 수 없습니다"));
        if (!calendar.getUserId().equals(userId)){
            throw new IllegalArgumentException("본인의 일정만 삭제할 수 있습니다.");
        }

        calendarRepository.delete(calendar);
    }
}
