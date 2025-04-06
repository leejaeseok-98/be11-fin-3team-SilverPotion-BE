package silverpotion.userserver.report.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.cors.PreFlightRequestHandler;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.domain.ReportStatus;
import silverpotion.userserver.report.dtos.*;
import silverpotion.userserver.report.repository.ReportRepository;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PreFlightRequestHandler preFlightRequestHandler;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository, PreFlightRequestHandler preFlightRequestHandler) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.preFlightRequestHandler = preFlightRequestHandler;
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

//    신고 목록 조회
    public Page<ReportResponseDto> findAllReports(String loginId, Pageable pageable, ReportRequestDto reportRequestDto){
        Specification<Report> spec = new Specification<Report>() {
            @Override
            public Predicate toPredicate(Root<Report> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();

//                신고 상태 필터링
                if (reportRequestDto.getReportStatus() != null){
                    predicates.add(criteriaBuilder.equal(root.get("reportStatus"),reportRequestDto.getReportStatus()));
                }
//                신고 대유형 필터링
                if (reportRequestDto.getReportBigCategory() != null){
                    predicates.add(criteriaBuilder.equal(root.get("reportBigCategory"),reportRequestDto.getReportBigCategory()));
                }
//                신고 소유형 필터링
                if (reportRequestDto.getReportSmallCategory() != null){
                    predicates.add(criteriaBuilder.equal(root.get("reportSmallCategory"),reportRequestDto.getReportSmallCategory()));
                }
//                신고자 이름 검색
                if (reportRequestDto.getName() != null){
                    Join<Report,User> reportUserJoin = root.join("reporter");
                    predicates.add(criteriaBuilder.like(reportUserJoin.get("name"),"%"+reportRequestDto.getName()+"%"));
                }
//                신고당한 유저 닉네임 검색
                if (reportRequestDto.getNickname() != null){
                    Join<Report,User> reportedUserJoin = root.join("reportedId");
                    predicates.add(criteriaBuilder.like(reportedUserJoin.get("nickName"),"%"+reportRequestDto.getNickname()+"%"));
                }
//                AND 조건결합
                Predicate[] predicateArr = new Predicate[predicates.size()];
                for (int i = 0; i < predicates.size(); i++) {
                    predicateArr[i] = predicates.get(i);
                }
                Predicate predicate = criteriaBuilder.and(predicateArr);
                return predicate;
            }
        };
//        페이징과 검색을 함께 적용하여 조회
        Page<Report> reportPage = reportRepository.findAll(spec,pageable);
        return reportPage.map(ReportResponseDto::fromReport);
    }

//    특정 신고 상세 조회
    public ReportDetailListDto getReportDetails(Long reportId){
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new EntityNotFoundException("Report Not Found"));
        String reportedName = report.getReportedId().getName();
        return ReportDetailListDto.reportDetailListDto(report,reportedName);
    }

//   관리자 신고 처리 기능
    public ReportProcessResDto processReport(Long reportId, ReportProcessResDto reportProcessResDto){
        Report report = reportRepository.findById(reportId).orElseThrow(()-> new EntityNotFoundException("Report Not Found"));
        //신고 상태 변경 & 관리자 코멘트 추가
        report.updateStatusAndDelete(reportProcessResDto.getReportStatus(),reportProcessResDto.getAdminComment());
        reportRepository.save(report);
        return reportProcessResDto;
    }
}
