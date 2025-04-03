package silverpotion.postserver.meeting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.meeting.dto.MeetingCreateDto;
import silverpotion.postserver.meeting.dto.MeetingInfoDto;
import silverpotion.postserver.meeting.service.MeetingService;

import java.util.List;

@RestController
@RequestMapping("silverpotion/meeting")
public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // 정모 생성
    @PostMapping("/create")
    public ResponseEntity<String> createMeeting(
            @RequestHeader("X-User-Id") String loginId,
            @ModelAttribute MeetingCreateDto dto) {

        meetingService.createMeeting(loginId, dto);
        return ResponseEntity.ok("모임이 성공적으로 생성되었습니다.");
    }

    // 모임별 정모 조회
    @GetMapping("/{gatheringId}/list")
    public List<MeetingInfoDto> getMeetingsByGatheringId(@PathVariable Long gatheringId) {
        return meetingService.getMeetingsByGatheringId(gatheringId);
    }
}
