package silverpotion.postserver.gatheringCategory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import silverpotion.postserver.gatheringCategory.domain.GatheringDetail;

public interface GatheringDetailRepository extends JpaRepository<GatheringDetail,Long> {
}
