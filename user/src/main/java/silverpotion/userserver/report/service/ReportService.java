package silverpotion.userserver.report.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.domain.ReportStatus;
import silverpotion.userserver.report.dtos.ReportCreateResDto;
import silverpotion.userserver.report.repository.ReportRepository;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    public Report reportCreate(ReportCreateResDto reportCreateResDto,String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(()-> new EntityNotFoundException("User Not Found"));
        Long reporterId = user.getId(); //신고자 id
        if (reporterId == reportCreateResDto.getReportedId()){
            throw new EntityNotFoundException("Self Report");
        }

        User reportedUser = userRepository.findById(reportCreateResDto.getReportedId()).orElseThrow(()-> new EntityNotFoundException("User Not Found"));
//        중복신고 방지(신고자id, 신고당한 유저id, 신고유형,신고 id where 조건 걸어서 필터링)
        if (reportRepository.existsByReporterAndReportedIdAndReportBigCategoryAndReferenceId(user,reportedUser
                ,reportCreateResDto.getReportBigCategory(),reportCreateResDto.getReferenceId())){
            throw new EntityNotFoundException("Duplicate Report");
        }
        Report report = Report.builder()
                .reporter(user)
                .reportedId(reportedUser)
                .reportStatus(ReportStatus.WAIT)
                .reportBigCategory(reportCreateResDto.getReportBigCategory())
                .reportSmallCategory(reportCreateResDto.getReportSmallCategory())
                .referenceId(reportCreateResDto.getReferenceId())
                .content(reportCreateResDto.getContent())
                .build();

        reportRepository.save(report);
        return report;
    }
}
