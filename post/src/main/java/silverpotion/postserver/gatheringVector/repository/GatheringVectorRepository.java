package silverpotion.postserver.gatheringVector.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.gatheringVector.domain.GatheringVector;

@Repository
public interface GatheringVectorRepository extends JpaRepository<GatheringVector,Long> {



}
