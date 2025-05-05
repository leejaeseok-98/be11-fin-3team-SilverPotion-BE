package silverpotion.postserver.calendar.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.calendar.domain.Calendar;
import silverpotion.postserver.calendar.dtos.CalendarCreateResDto;
import silverpotion.postserver.calendar.dtos.CalendarDetailResDto;
import silverpotion.postserver.calendar.dtos.UpdateCalendarDto;
import silverpotion.postserver.calendar.service.CalendarService;
import silverpotion.postserver.common.dto.CommonDto;

import java.util.List;

@RestController
@RequestMapping("/silverpotion/calendar")
public class CalendarController {
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    //일정 등록
    @PostMapping("/create")
    public ResponseEntity<?> registerCalendar(@RequestHeader("X-User-LoginId") String loginId, @RequestBody CalendarCreateResDto dto){
        calendarService.createCalendar(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "일정 등록 완료",loginId),HttpStatus.CREATED);
    }

    //일정 조회
    @GetMapping("/my")
    public ResponseEntity<?> getMyCalendar(@RequestHeader("X-User-LoginId") String loginId){
        List<Calendar> calendars = calendarService.getCalendar(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "일정 조회 완료",calendars),HttpStatus.OK);
    }
    //일정 상세조회
    @GetMapping("/detail/{calendarId}")
    public ResponseEntity<?> getCalendarDetail(@PathVariable Long calendarId){
        CalendarDetailResDto dto = calendarService.getDetail(calendarId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"일정 상세 조회",dto),HttpStatus.OK);
    }

    //일정 수정
    @PutMapping("/update")
    public ResponseEntity<?> updateCalendar(@RequestHeader("X-User-LoginId") String loginId,@RequestBody UpdateCalendarDto dto){
        calendarService.updateCalendar(loginId,dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "일정 수정 완료",dto),HttpStatus.OK);
    }


    //일정 삭제
    @DeleteMapping("/delete/{calendarId}")
    public ResponseEntity<?> deleteCalendar(@RequestHeader("X-User-LoginId") String loginId,@PathVariable Long calendarId){
        calendarService.deleteCalendar(calendarId,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"일정 삭제 완료",calendarId),HttpStatus.OK);
    }

}
