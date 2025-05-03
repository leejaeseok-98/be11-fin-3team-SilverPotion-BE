package silverpotion.userserver.recommendation.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserVectorBefore {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long Id;
    //정서적 지원 필요성 (설문조사 9점 + 수면데이터 6점으로 3:2비율)
    private int empathyNeeds;
    //성취성(설문조사 6점)
    private int achieveScore;
    //사교성(설문조사 6점)
    private int connectivityScore;
    //활동성(설문조사 9점 + 활동점수(걸음,칼로리,운동습관) 9점으로 1:1 비율)
    private int energyScore;
//    @OneToOne
//    @JoinColumn(name = "userVector_id")
//    UserVector userVector;

}
