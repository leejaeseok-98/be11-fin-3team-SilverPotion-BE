package silverpotion.postserver.gatheringCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringDetail;

import java.util.List;

public interface GatheringDetailRepository extends JpaRepository<GatheringDetail,Long> {
    List<GatheringDetail> findByGatheringCategoryDetail_NameContaining(String name);
}
