package silverpotion.postserver.gathering.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.gathering.dto.*;
import silverpotion.postserver.gathering.service.GatheringService;

import java.util.List;

@RestController
@RequestMapping("silverpotion/gathering")
public class GatheringController {
    private final GatheringService gatheringService;

    public GatheringController(GatheringService gatheringService) {
        this.gatheringService = gatheringService;
    }


    // 모임생성
    @PostMapping("/create")
    public ResponseEntity<?> gatheringCreate(@RequestBody GatheringCreateDto dto, @RequestHeader("X-User-Id") String loginId) {
        List<Long> gatheringCategoryDetailIds = dto.getGatheringCategoryDetailIds();
        return ResponseEntity.ok(gatheringService.gatheringCreate(dto, loginId, gatheringCategoryDetailIds));
    }

    // 내 모임 조회
    @GetMapping("/mygatherings")
    public List<GatheringInfoDto> getMyGatherings(@RequestHeader("X-User-Id") String loginId) {
        return gatheringService.getMyGatherings(loginId);
    }

    // 모임 참여자 수 조회
    @GetMapping("/peoplecount/{gatheringId}")
    public GatheringPeopleCountDto getPeopleCount(@PathVariable Long gatheringId) {
        return gatheringService.getActivePeopleCount(gatheringId);
    }

    // 모임 상세 조회
    @GetMapping("/{gatheringId}")
    public ResponseEntity<GatheringInfoDto> getGatheringById(@PathVariable Long gatheringId) {
        GatheringInfoDto gatheringInfo = gatheringService.getGatheringById(gatheringId);
        return ResponseEntity.ok(gatheringInfo);
    }

    // 모임 검색
    @GetMapping("/search")
    public ResponseEntity<List<GatheringInfoDto>> searchGatherings(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String gatheringName) {

        List<GatheringInfoDto> result = gatheringService.searchGatherings(category, gatheringName);
        return ResponseEntity.ok(result);
    }

    // 모임별 userlist
    @GetMapping("/{gatheringId}/userList")
    public ResponseEntity<List<GatheringPeopleDto>> getGatheringUserList(@PathVariable Long gatheringId) {
        List<GatheringPeopleDto> userList = gatheringService.getGatheringUserList(gatheringId);
        return ResponseEntity.ok(userList);
    }

    // 모임 가입
    @PostMapping("/register")
    public ResponseEntity<Void> createGatheringPeople(
            @RequestHeader("X-User-Id") String loginId,
            @RequestBody GatheringPeopleCreateDto dto) {

        gatheringService.createGatheringPeople(dto, loginId);
        return ResponseEntity.ok().build();
    }

    // 모임원 상태 변경
    @PatchMapping("/peopleupdate/{gatheringPeopleId}")
    public ResponseEntity<String> updateGatheringPeopleStatus(
            @PathVariable Long gatheringPeopleId,
            @RequestHeader("X-User-Id") String loginId,
            @RequestBody GatheringPeopleUpdateDto dto) {

        gatheringService.updateGatheringPeopleStatus(gatheringPeopleId, loginId, dto);
        return ResponseEntity.ok("회원 상태가 성공적으로 변경되었습니다.");
    }

    // 모임장 양도
    @PatchMapping("/leaderchange/{gatheringId}")
    public ResponseEntity<String> changeLeader(
            @PathVariable Long gatheringId,
            @RequestHeader("X-User-Id") String loginId,
            @RequestBody LeaderChangeDto dto) {

        gatheringService.changeLeader(gatheringId, loginId, dto);
        return ResponseEntity.ok("모임장이 변경되었습니다.");
    }
}
