package silverpotion.postserver.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.gathering.domain.GatheringPeople;

import java.util.List;
import java.util.Optional;

@Repository
public interface GatheringPeopleRepository extends JpaRepository<GatheringPeople,Long> {
    @Query("SELECT DISTINCT gp2.userId " +
            "FROM GatheringPeople gp1 " +
            "JOIN GatheringPeople gp2 ON gp1.gathering.id = gp2.gathering.id " +
            "WHERE gp1.userId = :userId")
    List<Long> findMemberIdsInSameGatherings(@Param("userId") Long userId);
    List<GatheringPeople> findByUserId(Long userId);
    @Query("SELECT COUNT(gp) FROM GatheringPeople gp WHERE gp.gathering.id = :gatheringId AND gp.status = 'ACTIVATE'")
    Long countByGatheringIdAndStatusActivate(@Param("gatheringId") Long gatheringId);
    List<GatheringPeople> findByGatheringId(Long gatheringId);
    Optional<GatheringPeople> findByGatheringIdAndUserId(Long gatheringId, Long userId);
    boolean existsByGatheringIdAndUserId(Long gatheringId, Long userId);
}
