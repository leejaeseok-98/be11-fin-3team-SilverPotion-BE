package silverpotion.postserver.gatheringCategory.service;

import org.springframework.stereotype.Service;
import silverpotion.postserver.gatheringCategory.dto.GatheringCategoryDetailDto;
import silverpotion.postserver.gatheringCategory.dto.GatheringCategoryDto;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryDetailRepository;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GatheringCategoryService {
    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final GatheringCategoryDetailRepository gatheringCategoryDetailRepository;

    public GatheringCategoryService(GatheringCategoryRepository gatheringCategoryRepository, GatheringCategoryDetailRepository gatheringCategoryDetailRepository) {
        this.gatheringCategoryRepository = gatheringCategoryRepository;
        this.gatheringCategoryDetailRepository = gatheringCategoryDetailRepository;
    }

    public List<GatheringCategoryDto> getAllCategories() {
        return gatheringCategoryRepository.findAll().stream()
                .map(GatheringCategoryDto::new)
                .collect(Collectors.toList());
    }

    public List<GatheringCategoryDetailDto> getAllCategoryDetails() {
        return gatheringCategoryDetailRepository.findAll().stream()
                .map(GatheringCategoryDetailDto::new)
                .collect(Collectors.toList());
    }

}
