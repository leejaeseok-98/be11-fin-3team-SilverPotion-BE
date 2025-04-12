package silverpotion.postserver.opensearch;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MeetingSearchResultDto {
    private Long id;
    private String name;
    private String place;
    private String imageUrl;
    private String meetingDate; // 문자열로 저장 (날짜 쿼리에도 적합)
    private String meetingTime;
    private Long cost;
    private Long maxPeople;
    private Long gatheringId;
}
