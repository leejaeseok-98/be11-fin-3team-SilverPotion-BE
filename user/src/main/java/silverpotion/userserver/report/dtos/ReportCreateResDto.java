package silverpotion.userserver.report.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.ReportBigCategory;
import silverpotion.userserver.report.domain.ReportSmallCategory;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ReportCreateResDto {
    private Long referenceId; //신고 대상 id/참조id
    private ReportBigCategory reportBigCategory; //채팅,게시물 등
    private ReportSmallCategory reportSmallCategory; //욕설, 따돌림 등
    private String content;//내용
    private Long reportedId;//신고당한 id
}
