package silverpotion.userserver.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.report.domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Long, Report> {
}
