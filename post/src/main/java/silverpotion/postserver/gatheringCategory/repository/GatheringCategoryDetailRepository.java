package silverpotion.postserver.gatheringCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategoryDetail;

public interface GatheringCategoryDetailRepository extends JpaRepository<GatheringCategoryDetail,Long> {
}
