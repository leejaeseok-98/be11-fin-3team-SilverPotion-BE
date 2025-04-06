package silverpotion.userserver.openAi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.openAi.domain.HealthReport;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HealthReportRepository extends JpaRepository<HealthReport,Long> {
    Optional<HealthReport> findByHealthDataIdAndCreatedDate(Long id, LocalDate date);

}
