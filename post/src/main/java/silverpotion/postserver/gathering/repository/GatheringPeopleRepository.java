package silverpotion.postserver.gathering.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.postserver.gathering.domain.GatheringPeople;
import silverpotion.postserver.gathering.domain.Status;

import java.util.List;
import java.util.Optional;

@Repository
public interface GatheringPeopleRepository extends JpaRepository<GatheringPeople,Long> {
    //해당모임 유저id 조회
    @Query("SELECT gp.userId FROM GatheringPeople gp WHERE gp.gathering.id = :gatheringId")
    List<Long> findUserIdsByGatheringId(@Param("gatheringId") Long gatheringId);
    List<GatheringPeople> findByUserId(Long userId);
    @Query("SELECT COUNT(gp) FROM GatheringPeople gp WHERE gp.gathering.id = :gatheringId AND gp.status = 'ACTIVATE'")
    Long countByGatheringIdAndStatusActivate(@Param("gatheringId") Long gatheringId);
//    List<GatheringPeople> findByGatheringIdAndStatus(Long gatheringId, Status status);
    List<GatheringPeople> findByGatheringId(Long gatheringId);
    Optional<GatheringPeople> findByGatheringIdAndUserId(Long gatheringId, Long userId);
    boolean existsByGatheringIdAndUserIdAndStatus(Long gatheringId, Long userId, Status status);
    List<GatheringPeople> findAllByGatheringId(Long gatheringId);
    List<GatheringPeople> findByUserIdAndStatus(Long userId, Status status);

}
