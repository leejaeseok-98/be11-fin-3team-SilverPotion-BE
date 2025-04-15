package silverpotion.postserver.gathering.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.common.domain.DelYN;
import silverpotion.postserver.gathering.domain.Gathering;

import java.util.List;
import java.util.Optional;

@Repository
public interface GatheringRepository extends JpaRepository<Gathering,Long> {
    Optional<Gathering> findByIdAndDelYN(Long id, DelYN delYN);
    Optional<Gathering> findByGatheringNameAndDelYN(String gatheringName, DelYN delYN);
    List<Gathering> findByIdIn(List<Long> ids);
    List<Gathering> findByGatheringCategoryNameAndGatheringNameContaining(String category, String gatheringName);
    List<Gathering> findByGatheringCategoryName(String category);
    List<Gathering> findByGatheringNameContaining(String gatheringName);
    List<Gathering> findByGatheringCategoryNameAndGatheringNameContainingAndRegionContaining(String category, String gatheringName, String region);
    List<Gathering> findByGatheringCategoryNameAndRegionContaining(String category, String region);
    List<Gathering> findByGatheringNameContainingAndRegionContaining(String gatheringName, String region);
    List<Gathering> findByRegionContaining(String region);
    long countByLeaderId(Long leaderId);

}
