package silverpotion.postserver.opensearch;

import lombok.Builder;
import lombok.Data;
import silverpotion.postserver.meeting.domain.Meeting;

@Data
@Builder
public class MeetingIndexDto {
    private Long id;
    private String name;
    private String place;
    private String imageUrl;
    private String meetingDate; // 문자열로 저장 (날짜 쿼리에도 적합)
    private String meetingTime;
    private Long cost;
    private Long maxPeople;
    private Long gatheringId;
    private String delYN;

    public static MeetingIndexDto fromEntity(Meeting meeting) {
        return MeetingIndexDto.builder()
                .id(meeting.getId())
                .name(meeting.getName())
                .place(meeting.getPlace())
                .imageUrl(meeting.getImageUrl())
                .meetingDate(meeting.getMeetingDate().toString())
                .meetingTime(meeting.getMeetingTime().toString())
                .cost(meeting.getCost())
                .maxPeople(meeting.getMaxPeople())
                .gatheringId(meeting.getGathering().getId())
                .delYN(meeting.getDelYN().name())
                .build();
    }
}
