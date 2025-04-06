package silverpotion.userserver.report.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.dtos.ReportCreateResDto;
import silverpotion.userserver.report.service.ReportService;

@RestController
@RequestMapping("/silverpotion/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> reportCreate(@RequestBody ReportCreateResDto reportCreateResDto
            , @RequestHeader("X-User-Id") String loginId){
        Report report = reportService.reportCreate(reportCreateResDto,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.CREATED.value(),"신고 등록 완료",report),HttpStatus.CREATED);
    }
}
