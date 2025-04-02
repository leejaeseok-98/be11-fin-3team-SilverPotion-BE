package silverpotion.postserver.gathering.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gathering.domain.Gathering;

import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering,Long> {
    Optional<Gathering> findByIdAndDelYN(Long id, DelYN delYN);
    Optional<Gathering> findByGatheringNameAndDelYN(String gatheringName, DelYN delYN);

}
