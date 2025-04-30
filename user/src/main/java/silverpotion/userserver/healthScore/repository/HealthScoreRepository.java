package silverpotion.userserver.healthScore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.healthScore.domain.HealthScore;
import silverpotion.userserver.healthScore.domain.Type;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HealthScoreRepository extends JpaRepository<HealthScore,Long> {

    Optional<HealthScore> findByUserIdAndCreatedDateAndType(Long userId, LocalDate date, Type type);
}
