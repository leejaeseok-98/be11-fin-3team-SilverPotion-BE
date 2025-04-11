package silverpotion.postserver.opensearch;

import lombok.Data;

@Data
public class IntegratedSearchRequest {
    private GatheringSearchRequest gatheringSearchRequest;
    private MeetingSearchRequest meetingSearchRequest;
}
