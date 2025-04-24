package silverpotion.postserver.gathering.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.gathering.dto.*;
import silverpotion.postserver.gathering.service.GatheringService;
import silverpotion.postserver.meeting.dto.MeetingUpdateDto;
import silverpotion.postserver.meeting.service.MeetingService;
import silverpotion.postserver.opensearch.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("silverpotion/gathering")
public class GatheringController {
    private final GatheringService gatheringService;
//    private final OpenSearchService openSearchService;
    private final MeetingService meetingService;

    public GatheringController(GatheringService gatheringService, MeetingService meetingService
//                               , OpenSearchService openSearchService
    ) {
        this.gatheringService = gatheringService;
//        this.openSearchService = openSearchService;
        this.meetingService = meetingService;
    }


    // 모임생성
    @PostMapping("/create")
    public ResponseEntity<?> gatheringCreate(@RequestBody GatheringCreateDto dto, @RequestHeader("X-User-LoginId") String loginId) {
        List<Long> gatheringCategoryDetailIds = dto.getGatheringCategoryDetailIds();
        gatheringService.gatheringCreateWithChat(dto, loginId, gatheringCategoryDetailIds);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "모임이 생성되었습니다.", dto), HttpStatus.OK);
    }

    // 모임 수정
    @PatchMapping("/update/{gatheringId}")
    public ResponseEntity<?> updateGathering(
            @RequestHeader("X-User-LoginId") String loginId,
            @PathVariable Long gatheringId,
            @ModelAttribute GatheringUpdateDto dto) {
        gatheringService.updateGathering(loginId, gatheringId, dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "모임이 정보가 수정되었습니다.", gatheringId), HttpStatus.OK);
    }

    // 내 모임 조회
    @GetMapping("/mygatherings")
    public ResponseEntity<?> getMyGatherings(@RequestHeader("X-User-LoginId") String loginId) {
        List<GatheringInfoDto> dtos = gatheringService.getMyGatherings(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "내가 속한 모임들이 조회되었습니다.", dtos), HttpStatus.OK);
    }

    // 모임 참여자 수 조회
    @GetMapping("/peoplecount/{gatheringId}")
    public ResponseEntity<?> getPeopleCount(@PathVariable Long gatheringId) {
        GatheringPeopleCountDto dto = gatheringService.getActivePeopleCount(gatheringId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해당 모임의 참여자 수가 조회되었습니다.", dto), HttpStatus.OK);
    }

    // 모임 상세 조회
    @GetMapping("/{gatheringId}")
    public ResponseEntity<?> getGatheringById(@PathVariable Long gatheringId) {
        GatheringInfoDto gatheringInfo = gatheringService.getGatheringById(gatheringId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해당 모임의 상세정보가 조회되었습니다.", gatheringInfo), HttpStatus.OK);
    }

    // 모임 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchGatherings(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String gatheringName,
            @RequestParam(required = false) String region,
            @RequestParam(required = false) String categoryDetail) {
        List<GatheringInfoDto> result = gatheringService.searchGatherings(category, gatheringName, region, categoryDetail);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "모임이 검색되었습니다.", result), HttpStatus.OK);
    }

    // 모임별 userlist
    @GetMapping("/{gatheringId}/userList")
    public ResponseEntity<?> getGatheringUserList(@PathVariable Long gatheringId) {
        List<GatheringPeopleDto> userList = gatheringService.getGatheringUserList(gatheringId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해당 모임의 유저들이 조회되었습니다.", userList), HttpStatus.OK);
    }

    // 모임 가입
    @PostMapping("/register")
    public ResponseEntity<?> createGatheringPeople(
            @RequestHeader("X-User-LoginId") String loginId,
            @RequestBody GatheringPeopleCreateDto dto) {

        gatheringService.createGatheringPeople(dto, loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "모임에 가입 신청되었습니다.", dto), HttpStatus.OK);
    }

    // 모임원 상태 변경
    @PatchMapping("/peopleupdate/{gatheringPeopleId}")
    public ResponseEntity<?> updateGatheringPeopleStatus(
            @PathVariable Long gatheringPeopleId,
            @RequestHeader("X-User-LoginId") String loginId,
            @RequestBody GatheringPeopleUpdateDto dto) {

        gatheringService.updateGatheringPeopleStatus(gatheringPeopleId, loginId, dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해당 모임원의 상태가 변경되었습니다.", dto), HttpStatus.OK);
    }

    // 모임장 양도
    @PatchMapping("/leaderchange/{gatheringId}")
    public ResponseEntity<?> changeLeader(
            @PathVariable Long gatheringId,
            @RequestHeader("X-User-LoginId") String loginId,
            @RequestBody LeaderChangeDto dto) {

        gatheringService.changeLeader(gatheringId, loginId, dto);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "해당 유저로 모임장이 변경되었습니다.", dto), HttpStatus.OK);
    }

    // 모임 탈퇴
    @PatchMapping("/withdraw/{gatheringId}")
    public ResponseEntity<?> withdrawFromGathering(
            @PathVariable Long gatheringId,
            @RequestHeader("X-User-LoginId") String loginId) {
        gatheringService.withdrawFromGathering(gatheringId, loginId);

        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "탈퇴가 완료되었습니다.", gatheringId), HttpStatus.OK);
    }

    // 모임 해체
    @PatchMapping("/disband/{gatheringId}")
    public ResponseEntity<?> disbandGathering(
            @PathVariable Long gatheringId,
            @RequestHeader("X-User-LoginId") String loginId) {
        gatheringService.disbandGathering(gatheringId, loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "모임이 해체되었습니다.", gatheringId), HttpStatus.OK);
    }

//    // OpenSearch
//    @PostMapping("/opensearch")
//    public ResponseEntity<?> search(@RequestBody IntegratedSearchRequest request) {
//        List<GatheringSearchResultDto> gatheringResults = gatheringService.searchGatherings(request.getGatheringSearchRequest());
//        List<MeetingSearchResultDto> meetingResults = meetingService.searchMeetings(request.getMeetingSearchRequest());
//
//        IntegratedSearchResultDto result = IntegratedSearchResultDto.builder()
//                .gatherings(gatheringResults)
//                .meetings(meetingResults)
//                .build();
//
//        return ResponseEntity.ok(new CommonDto(HttpStatus.OK.value(), "검색 성공", result));
//    }
//
//    // 자동완성
//    @GetMapping("/suggest")
//    public ResponseEntity<?> suggestAll(@RequestParam String prefix) {
//        List<String> suggestions = openSearchService.suggestAll(prefix);
//        return ResponseEntity.ok(new CommonDto(HttpStatus.OK.value(), "자동완성 성공", suggestions));
//    }
}
