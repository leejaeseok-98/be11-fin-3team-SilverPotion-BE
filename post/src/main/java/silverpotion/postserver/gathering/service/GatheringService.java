package silverpotion.postserver.gathering.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gathering.domain.Gathering;
import silverpotion.postserver.gathering.dto.GatheringCreateDto;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategoryDetail;
import silverpotion.postserver.gatheringCategory.domain.GatheringDetail;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryDetailRepository;
import silverpotion.postserver.gatheringCategory.repository.GatheringCategoryRepository;
import silverpotion.postserver.gathering.repository.GatheringRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;
import silverpotion.postserver.gatheringCategory.repository.GatheringDetailRepository;
import silverpotion.postserver.post.UserClient.UserClient;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class GatheringService {

    private final GatheringRepository gatheringRepository;
    private final GatheringCategoryRepository gatheringCategoryRepository;
    private final UserClient userClient;
    private final GatheringCategoryDetailRepository gatheringCategoryDetailRepository;
    private final GatheringDetailRepository gatheringDetailRepository;

    public GatheringService(GatheringRepository gatheringRepository, GatheringCategoryRepository gatheringCategoryRepository, UserClient userClient, GatheringCategoryDetailRepository gatheringCategoryDetailRepository, GatheringDetailRepository gatheringDetailRepository) {
        this.gatheringRepository = gatheringRepository;
        this.gatheringCategoryRepository = gatheringCategoryRepository;
        this.userClient = userClient;
        this.gatheringCategoryDetailRepository = gatheringCategoryDetailRepository;
        this.gatheringDetailRepository = gatheringDetailRepository;
    }


    public Long gatheringCreate(GatheringCreateDto dto, String loginId, List<Long> gatheringCategoryDetailIds) {
        if(gatheringRepository.findByGatheringNameAndDelYN(dto.getGatheringName(), DelYN.N).isPresent()){
            throw new IllegalArgumentException("이미 사용중인 모임명입니다");
        }

        GatheringCategory gatheringCategory = gatheringCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리 ID입니다."));

        Long leaderId = userClient.getUserIdByLoginId(loginId);
        Gathering gathering = gatheringRepository.save(dto.toEntity(gatheringCategory, leaderId));

        // 선택한 GatheringCategoryDetail을 기반으로 GatheringDetail 생성
        List<GatheringDetail> gatheringDetails = new ArrayList<>();
        for (Long gatheringCategoryDetailId : gatheringCategoryDetailIds) {
            GatheringCategoryDetail gatheringCategoryDetail = gatheringCategoryDetailRepository.findById(gatheringCategoryDetailId)
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 GatheringCategoryDetail ID입니다."));

            GatheringDetail gatheringDetail = new GatheringDetail(gathering, gatheringCategoryDetail);
            gatheringDetails.add(gatheringDetail);
        }

        // GatheringDetail 저장
        gatheringDetailRepository.saveAll(gatheringDetails);
        return gathering.getId();
    }

}
