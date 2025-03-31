package silverpotion.userserver.healthData.reopisitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.healthData.domain.HealthData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData,Long> {

    Optional<HealthData> findByUserIdAndCreatedDate(Long userId, LocalDate today);

    List<HealthData> findByUserIdAndCreatedDateBetween(Long userId,LocalDate start,LocalDate end);


}