package silverpotion.postserver.meeting.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import silverpotion.postserver.common.service.ImageService;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.meeting.domain.Meeting;
import silverpotion.postserver.meeting.domain.MeetingParticipant;
import silverpotion.postserver.meeting.dto.AttendeeDto;
import silverpotion.postserver.meeting.dto.MeetingCreateDto;
import silverpotion.postserver.meeting.dto.MeetingInfoDto;
import silverpotion.postserver.meeting.repository.MeetingParticipantRepository;
import silverpotion.postserver.meeting.repository.MeetingRepository;
import silverpotion.postserver.post.UserClient.UserClient;
import silverpotion.postserver.post.dtos.UserProfileInfoDto;

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

    public MeetingService(MeetingRepository meetingRepository, GatheringRepository gatheringRepository, UserClient userClient, ImageService imageService, MeetingParticipantRepository meetingParticipantRepository) {
        this.meetingRepository = meetingRepository;
        this.gatheringRepository = gatheringRepository;
        this.userClient = userClient;
        this.imageService = imageService;
        this.meetingParticipantRepository = meetingParticipantRepository;
    }

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
    }

    // 모임별 정모 조회
    public List<MeetingInfoDto> getMeetingsByGatheringId(Long gatheringId) {
        List<Meeting> meetings = meetingRepository.findByGatheringId(gatheringId);

        return meetings.stream().map(meeting -> {
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

}
