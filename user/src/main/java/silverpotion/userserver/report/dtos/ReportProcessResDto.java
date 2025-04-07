package silverpotion.userserver.report.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.ReportStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportProcessResDto {
    @NotNull(message = "관리자 코멘트는 필수입니다")
    private String adminComment;

    @NotNull(message = "처리 상태는 필수입니다")
    private ReportStatus reportStatus;

}
