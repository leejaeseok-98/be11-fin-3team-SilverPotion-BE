package silverpotion.userserver.report.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.Report;
import silverpotion.userserver.report.domain.ReportBigCategory;
import silverpotion.userserver.report.domain.ReportSmallCategory;
import silverpotion.userserver.report.domain.ReportStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportResponseDto {
    private Long reportId; //신고 ID
    private String reporterNickname; //신고한  사용자 id
    private String reportedNickname; //신고당한 사용자 id
    private ReportBigCategory reportBigCategory; //신고 대유형(채팅, 게시물 등)
    private ReportSmallCategory reportSmallCategory; //신고 소유형(욕설, 따돌림 등)
    private Long referenceId; //참조 id
    private String content; //내용
    private ReportStatus reportStatus; //신고처리 상태
    private LocalDateTime reportedTime; // 신고된 시간


    public static ReportResponseDto fromReport(Report report,String reportedNickname){
        System.out.println(report.getReportBigCategory());
        System.out.println(report.getReportSmallCategory());
        return ReportResponseDto.builder()
                .reportId(report.getId())
                .reporterNickname(report.getReporter().getNickName())
                .reportedNickname(reportedNickname)
                .reportBigCategory(report.getReportBigCategory())
                .reportSmallCategory(report.getReportSmallCategory())
                .referenceId(report.getReferenceId())
                .content(report.getContent())
                .reportStatus(report.getReportStatus())
                .reportedTime(report.getCreatedTime())
                .build();
    }
}
