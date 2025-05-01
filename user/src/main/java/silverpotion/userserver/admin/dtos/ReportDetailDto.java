package silverpotion.userserver.admin.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.ReportBigCategory;
import silverpotion.userserver.report.domain.ReportSmallCategory;
import silverpotion.userserver.report.domain.ReportStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportDetailDto {
    private Long id; //신고id
    private Long reporterId; //신고한 사용자 id
    private Long reportedId; //신고당한 사용자id
    private ReportBigCategory reportBigCategory;// 채팅, 게시물, 유저 등
    private ReportSmallCategory reportSmallCategory;//욕설, 사기 등
    private Long referenceId; //참고ID
    private String content;//신고 상세 내용
    private String adminComment; //관리자 멘트
    private ReportStatus reportStatus; //신고처리상태
    private LocalDateTime reportedTime;//신고된 시간
}
