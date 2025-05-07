package silverpotion.userserver.recommendation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import silverpotion.userserver.user.domain.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class UserVector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //정서적 지원 필요성 (설문조사 9점 + 수면데이터 6점으로 3:2비율)
    private double empathyNeeds;
    //성취성 (설문조사 6점)
    private double achievement;
    //사교성 (설문조사 6점)
    private double connectivity;
    //활동성 (설문조사 9점 + 활동점수(걸음,칼로리,운동습관) 9점으로 1:1 비율)
    private double energy;
    //유저
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
//    //절대값
//    @OneToOne(mappedBy = "user_vector", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private UserVectorBefore userVectorBefore;

}
