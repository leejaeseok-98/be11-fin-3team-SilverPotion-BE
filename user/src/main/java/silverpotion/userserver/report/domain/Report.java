package silverpotion.userserver.report.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Negative;
import lombok.*;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Report extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User reporter; //신고자id

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportBigCategory reportBigCategory;//어디 유형에서 온 신고인지(채팅,게시물 등)

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReportSmallCategory reportSmallCategory; //어떤 유형에 신고인지(욕설, 따돌림 등)

    @Column(nullable = false)
    private String content; //신고내용

    @Column(nullable = false)
    private Long referenceId;//참조 id

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus = ReportStatus.WAIT;//신고처리상태(대기, 완료)

    private String adminComment; //관리자 코멘트

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_id")
    private User reportedId; //신고당한 id

    private LocalDateTime processedAt;//신고처리시간

    @Column(nullable = false)
    @Builder.Default
    private DelYN delYn = DelYN.N;

    // 신고 상태 변경 메서드
    public void updateStatusAndDelete(ReportStatus status, String adminComment) {
        this.reportStatus = status;
        this.adminComment = adminComment;
        this.processedAt = LocalDateTime.now(); // 처리된 시간 갱신
        this.delYn = DelYN.Y;
    }
}
