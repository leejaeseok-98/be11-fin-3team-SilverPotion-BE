package silverpotion.postserver.meeting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.meeting.dto.MeetingCreateDto;
import silverpotion.postserver.meeting.service.MeetingService;

@RestController
@RequestMapping("silverpotion/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createMeeting(
            @RequestHeader("X-User-Id") String loginId,
            @ModelAttribute MeetingCreateDto dto) {

        meetingService.createMeeting(loginId, dto);
        return ResponseEntity.ok("모임이 성공적으로 생성되었습니다.");
    }
}
