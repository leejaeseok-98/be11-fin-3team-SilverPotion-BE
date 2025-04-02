package silverpotion.postserver.gathering.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.postserver.gathering.dto.GatheringCreateDto;
import silverpotion.postserver.gathering.service.GatheringService;

import java.util.List;

@RestController
@RequestMapping("silverpotion/gathering")
public class GatheringController {
    private final GatheringService gatheringService;

    public GatheringController(GatheringService gatheringService) {
        this.gatheringService = gatheringService;
    }



    @PostMapping("/create")
    public ResponseEntity<?> gatheringCreate(@RequestBody GatheringCreateDto dto, @RequestHeader("X-User-Id") String loginId) {
        List<Long> gatheringCategoryDetailIds = dto.getGatheringCategoryDetailIds();
        return ResponseEntity.ok(gatheringService.gatheringCreate(dto, loginId, gatheringCategoryDetailIds));
    }
}
