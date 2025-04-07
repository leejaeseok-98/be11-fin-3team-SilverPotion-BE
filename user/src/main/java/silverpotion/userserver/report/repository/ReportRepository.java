package silverpotion.userserver.report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.domain.ReportBigCategory;
import silverpotion.userserver.user.domain.User;

@Repository
public interface ReportRepository extends JpaRepository<Report,Long>, JpaSpecificationExecutor<Report> {
    boolean existsByReporterAndReportedIdAndReportBigCategoryAndReferenceId(
            User reporter,
            User reportedId,
            ReportBigCategory reportBigCategory,
            Long referenceId
    );
}
