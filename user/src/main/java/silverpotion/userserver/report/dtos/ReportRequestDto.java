package silverpotion.userserver.report.dtos;

import jakarta.persistence.Access;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.report.domain.ReportBigCategory;
import silverpotion.userserver.report.domain.ReportSmallCategory;
import silverpotion.userserver.report.domain.ReportStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReportRequestDto {
    private String nickname;
    private String name;
    private ReportBigCategory reportBigCategory;
    private ReportSmallCategory reportSmallCategory;
    private ReportStatus reportStatus;
}
