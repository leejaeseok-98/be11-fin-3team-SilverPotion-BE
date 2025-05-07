package silverpotion.userserver.careRelation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.careRelation.domain.LinkStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CareRelationRepository extends JpaRepository<CareRelation,Long> {
    Optional<CareRelation> findByIdAndLinkStatus(Long id, LinkStatus linkStatus);
    List<CareRelation> findByDependentIdAndLinkStatus(Long id, LinkStatus linkStatus);

    // 피보호자 수 조회
    @Query("SELECT COUNT(cr) FROM CareRelation cr WHERE cr.protector.id = :userId AND cr.linkStatus = 'CONNECTED'")
    int countDependentsByProtectorId(@Param("userId") Long userId);
    // 보호자 수 조회
    @Query("SELECT COUNT(cr) FROM CareRelation cr WHERE cr.dependent.id = :userId AND cr.linkStatus = 'CONNECTED'")
    int countProtectorsByDependentId(@Param("userId") Long userId);
}
