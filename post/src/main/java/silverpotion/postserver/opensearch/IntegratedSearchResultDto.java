package silverpotion.postserver.opensearch;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IntegratedSearchResultDto {
    private List<GatheringSearchResultDto> gatherings;
    private List<MeetingSearchResultDto> meetings;
}
