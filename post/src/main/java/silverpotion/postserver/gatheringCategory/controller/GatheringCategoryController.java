package silverpotion.postserver.gatheringCategory.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.postserver.common.dto.CommonDto;
import silverpotion.postserver.gatheringCategory.dto.GatheringCategoryDetailDto;
import silverpotion.postserver.gatheringCategory.dto.GatheringCategoryDto;
import silverpotion.postserver.gatheringCategory.service.GatheringCategoryService;

import java.util.List;

@RestController
@RequestMapping("silverpotion/gathering-category")
public class GatheringCategoryController {
    private final GatheringCategoryService gatheringCategoryService;

    public GatheringCategoryController(GatheringCategoryService gatheringCategoryService) {
        this.gatheringCategoryService = gatheringCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<GatheringCategoryDto> dto = gatheringCategoryService.getAllCategories();
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "카테고리가 조회되었습니다.", dto), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getAllCategoryDetails() {
        List<GatheringCategoryDetailDto> dto = gatheringCategoryService.getAllCategoryDetails();
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "상세 카테고리가 조회되었습니다.", dto), HttpStatus.OK);
    }
}
