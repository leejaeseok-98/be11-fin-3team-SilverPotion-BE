package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringUpdateDto {

    private String gatheringName;

    private String introduce;

    private Long maxPeople;

    private MultipartFile imageFile;
}
