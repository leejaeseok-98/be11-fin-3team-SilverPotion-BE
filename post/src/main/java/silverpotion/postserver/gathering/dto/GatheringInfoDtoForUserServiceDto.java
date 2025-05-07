package silverpotion.postserver.gathering.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GatheringInfoDtoForUserServiceDto {
    //모임 아이디
    private Long id;
    //모임 이름
    private String gatheringName;
    //모임 프로필 사진
    private String imageUrl;
    //모임지역
    private String region;
    //최대 정원
    private Long maxPeople;
    //대카테고리
    private String category;
    //소 카테고리
    private List<String> detailCategory;
    //소개글
    private String introduce;
    //현재 인원수
    private Long peopleCount;



}
