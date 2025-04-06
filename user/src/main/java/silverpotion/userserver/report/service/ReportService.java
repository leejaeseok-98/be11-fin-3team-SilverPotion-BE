package silverpotion.userserver.report.service;

import org.springframework.stereotype.Service;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.dtos.ReportCreateResDto;
import silverpotion.userserver.report.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public Report reportCreate(ReportCreateResDto reportCreateResDto,String loginId){

    }
}
