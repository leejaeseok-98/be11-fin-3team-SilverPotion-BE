package silverpotion.postserver.gatheringCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringCategory;

public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory,Long> {
}
