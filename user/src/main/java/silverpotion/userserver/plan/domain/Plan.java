package silverpotion.userserver.plan.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import silverpotion.userserver.common.domain.BaseTimeEntity;
import silverpotion.userserver.plan.dto.PlanDetailResDto;
import silverpotion.userserver.plan.dto.PlanListResDto;
import silverpotion.userserver.plan.dto.PlanUpdateDto;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plan extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //    @Column(nullable = false)
    private String title;

    //    @Column(nullable = false)
    private String content;

    //    @Column(nullable = false)
    private LocalDateTime startTime;

    //    @Column(nullable = false)
    private LocalDateTime endTime;

    private String userLoginId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private User user;

    public void toUpdate(PlanUpdateDto dto){
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }

        if (dto.getContent() != null) {
            this.content = dto.getContent();
        }

        if (dto.getStartTime() != null) {
            this.startTime = dto.getStartTime();
        }

        if (dto.getEndTime() != null) {
            this.endTime = dto.getEndTime();
        }
    }
    // 리스트
    public PlanListResDto listFromEntity(){
        return PlanListResDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }

    // 디테일
    public PlanDetailResDto detailFromEntity(){
        return PlanDetailResDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .startTime(this.startTime)
                .endTime(this.endTime)
                .build();
    }
}
