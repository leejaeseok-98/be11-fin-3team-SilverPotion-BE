package silverpotion.postserver.gatheringCategory.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public List<GatheringCategoryDto> getAllCategories() {
        return gatheringCategoryService.getAllCategories();
    }

    @GetMapping("/detail")
    public List<GatheringCategoryDetailDto> getAllCategoryDetails() {
        return gatheringCategoryService.getAllCategoryDetails();
    }
}
