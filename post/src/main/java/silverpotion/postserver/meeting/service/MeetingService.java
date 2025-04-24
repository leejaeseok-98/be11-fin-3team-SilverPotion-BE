package silverpotion.postserver.meeting.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.common.service.ImageService;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.meeting.domain.Meeting;
import silverpotion.postserver.meeting.domain.MeetingParticipant;
import silverpotion.postserver.meeting.dto.*;
import silverpotion.postserver.meeting.repository.MeetingParticipantRepository;
import silverpotion.postserver.meeting.repository.MeetingRepository;
//import silverpotion.postserver.opensearch.OpenSearchService;
import silverpotion.postserver.post.feignClient.UserClient;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final GatheringRepository gatheringRepository;
    private final UserClient userClient;
    private final ImageService imageService;
    private final MeetingParticipantRepository meetingParticipantRepository;
//    private final OpenSearchService openSearchService;

    public MeetingService(MeetingRepository meetingRepository, GatheringRepository gatheringRepository, UserClient userClient, ImageService imageService, MeetingParticipantRepository meetingParticipantRepository
//                          , OpenSearchService openSearchService
    ) {
        this.meetingRepository = meetingRepository;
        this.gatheringRepository = gatheringRepository;
        this.userClient = userClient;
        this.imageService = imageService;
        this.meetingParticipantRepository = meetingParticipantRepository;
//        this.openSearchService = openSearchService;
    }

    // 정모 생성
    public void createMeeting(String loginId, MeetingCreateDto dto) {
        // 로그인 ID로 userId 조회
        Long userId = userClient.getUserIdByLoginId(loginId);

        // Gathering 조회
        Gathering gathering = gatheringRepository.findById(dto.getGatheringId())
                .orElseThrow(() -> new IllegalArgumentException("해당 모임이 존재하지 않습니다."));

        // 모임장만 생성 가능하도록 체크
        if (!gathering.getLeaderId().equals(userId)) {
            throw new IllegalArgumentException("모임장만 정모를 생성할 수 있습니다.");
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 날짜 & 시간 검증
        if (dto.getMeetingDate() != null) {
            if (dto.getMeetingDate().isBefore(today)) {
                throw new IllegalArgumentException("정모 날짜는 현재 날짜 이후로만 설정할 수 있습니다.");
            }
            if (dto.getMeetingDate().isEqual(today) && dto.getMeetingTime() != null) {
                if (dto.getMeetingTime().isBefore(now)) {
                    throw new IllegalArgumentException("오늘 날짜의 정모는 현재 시간 이후로만 설정할 수 있습니다.");
                }
            }
        }

        // 이미지 업로드 (S3)
        String imageUrl = null;
        MultipartFile imageFile = dto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = imageService.uploadImage(imageFile);
        }

        // Meeting 저장
        Meeting meeting = Meeting.builder()
                .gathering(gathering)
                .name(dto.getName())
                .meetingDate(dto.getMeetingDate())
                .meetingTime(dto.getMeetingTime())
                .place(dto.getPlace())
                .cost(dto.getCost())
                .maxPeople(dto.getMaxPeople())
                .imageUrl(imageUrl)
                .build();

        meetingRepository.save(meeting);

        // 모임장(leader)을 자동으로 MeetingParticipant로 추가
        MeetingParticipant participant = MeetingParticipant.builder()
                .meeting(meeting)
                .userId(userId)
                .build();

        meetingParticipantRepository.save(participant);

        // OpenSearch index 저장
//        openSearchService.indexMeeting(meeting);
    }

    // 정모 수정
    public void updateMeeting(String loginId, Long meetingId, MeetingUpdateDto dto) {
        // 로그인 ID로 userId 조회
        Long userId = userClient.getUserIdByLoginId(loginId);

        // Meeting 조회
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정모가 존재하지 않습니다."));

        // Gathering 조회 및 모임장 검증
        Gathering gathering = meeting.getGathering();
        if (!gathering.getLeaderId().equals(userId)) {
            throw new IllegalArgumentException("모임장만 정모를 수정할 수 있습니다.");
        }

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        // 날짜 & 시간 검증
        if (dto.getMeetingDate() != null) {
            if (dto.getMeetingDate().isBefore(today)) {
                throw new IllegalArgumentException("정모 날짜는 현재 날짜 이후로만 설정할 수 있습니다.");
            }
            if (dto.getMeetingDate().isEqual(today) && dto.getMeetingTime() != null) {
                if (dto.getMeetingTime().isBefore(now)) {
                    throw new IllegalArgumentException("오늘 날짜의 정모는 현재 시간 이후로만 설정할 수 있습니다.");
                }
            }
        }

        // 이미지 업로드 (새로운 이미지가 있는 경우 S3 업데이트)
        String imageUrl = meeting.getImageUrl();
        MultipartFile imageFile = dto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = imageService.uploadImage(imageFile);
        }

        // Meeting 정보 업데이트 (null 체크 후 수정)
        if (dto.getName() != null) meeting.setName(dto.getName());
        if (dto.getMeetingDate() != null) meeting.setMeetingDate(dto.getMeetingDate());
        if (dto.getMeetingTime() != null) meeting.setMeetingTime(dto.getMeetingTime());
        if (dto.getPlace() != null) meeting.setPlace(dto.getPlace());
        if (dto.getCost() != null) meeting.setCost(dto.getCost());
        if (dto.getMaxPeople() != null) meeting.setMaxPeople(dto.getMaxPeople());
        meeting.setImageUrl(imageUrl);

        meetingRepository.save(meeting);

        // OpenSearch index 저장
//        openSearchService.indexMeeting(meeting);
    }

    // 모임별 정모 조회
    public List<MeetingInfoDto> getMeetingsByGatheringId(Long gatheringId) {
        List<Meeting> meetings = meetingRepository.findByGatheringId(gatheringId);

        LocalDateTime now = LocalDateTime.now();

        return meetings.stream()
                .filter(meeting -> {
                    LocalDateTime meetingDateTime = LocalDateTime.of(meeting.getMeetingDate(), meeting.getMeetingTime());
                    return meetingDateTime.isAfter(now);
                })
                .map(meeting -> {
            // 해당 미팅의 모든 참가자 가져오기
            List<MeetingParticipant> participants = meetingParticipantRepository.findByMeetingId(meeting.getId());

            // 참가자 정보를 AttendeeDto 리스트로 변환
            List<AttendeeDto> attendees = participants.stream().map(participant -> {
                UserProfileInfoDto profileInfo = userClient.getUserProfileInfo(participant.getUserId());
                return new AttendeeDto(participant.getUserId(), profileInfo.getNickname(), profileInfo.getProfileImage());
            }).collect(Collectors.toList());

            // MeetingInfoDto 생성 후 반환
            return new MeetingInfoDto(
                    meeting.getId(),
                    meeting.getGathering().getId(),
                    meeting.getName(),
                    meeting.getMeetingDate(),
                    meeting.getMeetingTime(),
                    meeting.getPlace(),
                    meeting.getImageUrl(),
                    meeting.getCost(),
                    meeting.getMaxPeople(),
                    attendees
            );
        }).collect(Collectors.toList());
    }

    // 정모 참석
    public void attendMeeting(String loginId, MeetingAttendDto dto) {
        // 로그인 ID로 userId 조회
        Long userId = userClient.getUserIdByLoginId(loginId);

        // Meeting 조회
        Meeting meeting = meetingRepository.findById(dto.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("해당 정모가 존재하지 않습니다."));

        // 이미 참여한 경우 예외 처리
        boolean alreadyParticipated = meetingParticipantRepository.existsByMeetingIdAndUserId(dto.getMeetingId(), userId);
        if (alreadyParticipated) {
            throw new IllegalStateException("이미 해당 정모에 참여하였습니다.");
        }

        // MeetingParticipant 저장
        MeetingParticipant meetingParticipant = MeetingParticipant.builder()
                .meeting(meeting)
                .userId(userId)
                .build();

        meetingParticipantRepository.save(meetingParticipant);
    }

    // 정모 참석 취소
    public void cancelAttendance(String loginId, MeetingAttendDto dto) {
        Long userId = userClient.getUserIdByLoginId(loginId);

        Meeting meeting = meetingRepository.findById(dto.getMeetingId())
                .orElseThrow(() -> new IllegalArgumentException("해당 정모가 존재하지 않습니다."));

        MeetingParticipant participant = meetingParticipantRepository.findByMeetingAndUserId(meeting, userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 정모에 참석하지 않았습니다."));

        meetingParticipantRepository.delete(participant);
    }

    // 정모 상세 조회
    public MeetingInfoDto getMeetingById(Long meetingId) {
        Meeting meeting = meetingRepository.findByIdAndDelYN(meetingId, DelYN.N)
                .orElseThrow(() -> new EntityNotFoundException("해당 정모를 찾을 수 없습니다."));

        // 해당 미팅의 모든 참가자 가져오기
        List<MeetingParticipant> participants = meetingParticipantRepository.findByMeetingId(meeting.getId());

        // 참가자 정보를 AttendeeDto 리스트로 변환
        List<AttendeeDto> attendees = participants.stream().map(participant -> {
            UserProfileInfoDto profileInfo = userClient.getUserProfileInfo(participant.getUserId());
            return new AttendeeDto(participant.getUserId(), profileInfo.getNickname(), profileInfo.getProfileImage());
        }).collect(Collectors.toList());

        return new MeetingInfoDto(
                meeting.getId(),
                meeting.getGathering().getId(),
                meeting.getName(),
                meeting.getMeetingDate(),
                meeting.getMeetingTime(),
                meeting.getPlace(),
                meeting.getImageUrl(),
                meeting.getCost(),
                meeting.getMaxPeople(),
                attendees
        );
    }

    // 다가오는 정모 조회
    public List<MeetingInfoDto> getMeetingsWithinAWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneWeekLater = now.plusDays(7);

        List<Meeting> meetings = meetingRepository.findAll();

        return meetings.stream()
                .filter(meeting -> {
                    LocalDateTime meetingDateTime = LocalDateTime.of(meeting.getMeetingDate(), meeting.getMeetingTime());
                    return !meetingDateTime.isBefore(now) && !meetingDateTime.isAfter(oneWeekLater);
                })
                .map(meeting -> {
                    List<MeetingParticipant> participants = meetingParticipantRepository.findByMeetingId(meeting.getId());

                    List<AttendeeDto> attendees = participants.stream().map(participant -> {
                        UserProfileInfoDto profileInfo = userClient.getUserProfileInfo(participant.getUserId());
                        return new AttendeeDto(participant.getUserId(), profileInfo.getNickname(), profileInfo.getProfileImage());
                    }).collect(Collectors.toList());

                    return new MeetingInfoDto(
                            meeting.getId(),
                            meeting.getGathering().getId(),
                            meeting.getName(),
                            meeting.getMeetingDate(),
                            meeting.getMeetingTime(),
                            meeting.getPlace(),
                            meeting.getImageUrl(),
                            meeting.getCost(),
                            meeting.getMaxPeople(),
                            attendees
                    );
                })
                .collect(Collectors.toList());
    }

//    // opensearch
//    public List<MeetingSearchResultDto> searchMeetings(MeetingSearchRequest request) {
//        return openSearchService.searchMeetings(request);
//    }

}
