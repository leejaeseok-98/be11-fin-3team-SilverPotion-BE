package silverpotion.userserver.recommendation.domain;


import jakarta.persistence.EntityNotFoundException;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthScore.domain.HealthScore;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;


//추천에 필요한 유틸리티 클래스
public class UserVectorUtils {


// 스코어는 현재점수, 맥스는 그 항목의 최대 점수
//    정규화하는 함수
    public static double normalize(int score, int max){
        if(max<=0) return 0.0;
        return Math.min(Math.max(score / (double)max, 0.0),1.0);
    }

    public static int sleepToEmpathyNeeds(HealthData healthData){
        int sleepMinutes = healthData.getTotalSleepMinutes();
        // 11시간 이상(660분 이상) 또는 5시간 미만(300분 미만) → 매우 부적절
        if (sleepMinutes >= 660 || sleepMinutes < 300) {
            return 6;
        }
        // 10시간 이상(600분 이상) 또는 6시간 미만(360분 미만) → 부적절
        else if (sleepMinutes >= 600 || sleepMinutes < 360) {
            return 4;
        }
        // 6시간 ~ 7시간(360~420분) → 약간 부족
        else if (sleepMinutes >= 360 && sleepMinutes < 420) {
            return 2;
        }
        // 7시간~9시간(420~540분) → 적절
        else {
            return 0;
        }
    }

    //활동점수를 에너지벡터에 더하기 위해 활동점수를 0~9점으로 변환하는 메서드
    public static double activityScoreToEnergyScore(HealthScore healthScore){
        int activityScore = healthScore.getActivityScore();
        return (activityScore /100.0) * 9.0;
    }

    //유저의 정규화된 벡터값 만들어주는 함수
    public static double[] makingUserVector(User user){
        //정서적 지원 필요성 (설문조사 9점 + 수면데이터 6점으로 3:2비율)
        int empathyNeeds =0;
        //성취성(설문조사 6점)
        int achieveScore =0;
        //사교성(설문조사 6점)
        int connectivityScore =0;
        //활동성(설문조사 9점 + 활동점수(걸음,칼로리,운동습관) 9점으로 1:1 비율)
        double energyScore =0;

        //벡터 값 계산에 필요한 사용자의 설문조사 객체
        UserDetailHealthInfo usersHealthInfo = user.getUserDetailHealthInfo();
        //벡터 값 계산에 필요한 사용자의 헬스데이터인데 월별이 있다면 월별데이터를 가지고 오고 없다면 주간 없다면 일간을 가져옴
        List<HealthData> healthDataList = user.getMyHealthData();
        HealthData selectedHealthdata = healthDataList.stream()
                .sorted(
                        Comparator
                                .comparingInt((HealthData d) -> d.getDataType().getPriority())  // 타입 우선순위 낮은 게 먼저(즉 월별,주간,일간)
                                .thenComparing(HealthData::getCreatedDate, Comparator.reverseOrder()) // 그러고나서 생성날짜가 제일 최신으로
                )
                .findFirst().orElseThrow(()->new EntityNotFoundException("일간 헬스 데이터도 없습니다"));
        //벡터 값 계산에 필요한 사용자의 헬스스코어인데 마찬 가지로 월별이 있다면 월별데이터를 가지고 오고 없다면 주간 없다면 일간을 가져옴
        List<HealthScore> healthScores = user.getHealthScores();
        HealthScore selectedHealthScore = healthScores.stream()
                .sorted(
                        Comparator
                                .comparingInt((HealthScore d) -> d.getType().getPriority())  // 타입 우선순위 낮은 게 먼저(즉 월별,주간,일간)
                                .thenComparing(HealthScore::getCreatedDate, Comparator.reverseOrder()) // 그러고나서 생성날짜가 제일 최신으로
                )
                .findFirst().orElseThrow(()->new EntityNotFoundException("일간 헬스 점수도 없습니다"));

        //정서적 지원 필요성에 필요한 데이터들
        int serveyScoreForEmpathy = usersHealthInfo.getEmpathyNeedScore();
        int sleepDataScore = sleepToEmpathyNeeds(selectedHealthdata);
        empathyNeeds = serveyScoreForEmpathy + sleepDataScore;
        //성취성
        achieveScore = usersHealthInfo.getAchieveScore();
        //사교성
        connectivityScore = usersHealthInfo.getPeopleConnectivityScore();
        //활동성
        int serveyScoreForActivity = usersHealthInfo.getEnergyScore();
        double activeHealthDataScore = activityScoreToEnergyScore(selectedHealthScore);
        energyScore = serveyScoreForActivity + activeHealthDataScore;

        return new double[]{
                normalize(empathyNeeds,15),normalize(achieveScore,6),normalize(connectivityScore,6),normalize((int)energyScore,18)
        };
    }

    //코사인 유사도
    //2개의 인자 2개의 벡터값
    public static double cosineSimilarity(double[]a, double[]b){
        double dot =0.0; //내적(Dot Product). 내적이란, 같은 차원의 벡터가 있을때 대응하는 요소끼리 곱해서 전부 더한 것으로 내적이 클수록 두 벡터는 비슷한 방향. 내적이 0이면 수직. 내적은 코사인 유사도의 분자 부분
        double normA =0.0; //여기서 norm은 벡터의 크기
        double normB =0.0; //벡터 b의 길이

        for(int i = 0; i< a.length; i++){
            dot += a[i]* b[i];
            normA += a[i] *a[i];
            normB += b[i] * b[i];
        }

        if(normA ==0 || normB ==0)return 0.0;

        return dot/(Math.sqrt(normA) *  Math.sqrt(normB));
    }

    //유클리드 거리 계산
    public static double euclideanDistance(double[]a, double[]b){
        if(a.length != b.length) throw new IllegalArgumentException("벡터간 길이 불일치");

        double sum = 0.0;
        for(int i=0; i<a.length; i++){
            sum +=Math.pow(a[i]-b[i],2); //벡터의 각 차이의 제곱을 sum에 더함
        }
        return Math.sqrt(sum);
    }







}
