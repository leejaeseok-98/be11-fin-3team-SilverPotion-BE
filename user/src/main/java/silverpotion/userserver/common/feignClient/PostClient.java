package silverpotion.userserver.common.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import silverpotion.userserver.recommendation.dtos.GatheringInfoDtoForUserServiceDto;
import silverpotion.userserver.recommendation.dtos.GatheringVectorForUserServiceDto;

import java.util.List;

@FeignClient(name = "post-service", configuration = FeignConfig.class)
public interface PostClient {

    @GetMapping("/silverpotion/gatheringvector/allvectors")
    public List<GatheringVectorForUserServiceDto> allGatheringsForUserService();


    @PostMapping("silverpotion/gathering/fivegatherings")
    public List<GatheringInfoDtoForUserServiceDto> fiveRecommendedGatherings(@RequestBody List<Long> gatheringIds);



}
