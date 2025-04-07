package silverpotion.userserver.report.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.domain.ReportStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportDetailListDto {
    private Long reportId;
    private String name;//신고당한 사람 이름
    private Long reportedId;
    private String content;
    private String adminComment;
    private ReportStatus reportStatus;

    public static ReportDetailListDto reportDetailListDto(Report report,String reportedName){
        return ReportDetailListDto.builder()
                .reportId(report.getId())
                .name(reportedName)
                .reportedId(report.getReportedId().getId())
                .content(report.getContent())
                .adminComment(report.getAdminComment())
                .reportStatus(report.getReportStatus())
                .build();
    }
}
