package silverpotion.postserver.gatheringVector.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.postserver.gatheringVector.dtos.GatheringVectorForUserServiceDto;
import silverpotion.postserver.gatheringVector.service.GatheringVectorService;
import java.util.List;

@RestController
@RequestMapping("/silverpotion/gatheringvector")
public class GatheringVectorController {

    private final GatheringVectorService gatheringVectorService;

    public GatheringVectorController(GatheringVectorService gatheringVectorService) {
        this.gatheringVectorService = gatheringVectorService;
    }


    //    1. 페인클라이언트 용 모든 GatheringVector 리턴
    @GetMapping("/allvectors")
    public List<GatheringVectorForUserServiceDto> allGatheringsForUserService(){

       return gatheringVectorService.getAllGatherings();
    }

}
