package silverpotion.userserver.healthScore.domain;

import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.user.domain.Sex;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.userDetailHealthInfo.domain.UserDetailHealthInfo;

import java.util.Map;
import java.util.Objects;

public class HealthScoreUtils {

//    1.활동점수 도출 메서드
//    걸음수 도달점수(50), 칼로리 도달 점수(20), 운동습관(30)
    public static int whatsMyActivityScore(HealthData healthData, UserDetailHealthInfo info,User user){
        int stepCounts = healthData.getStep();
        int activeCalories = healthData.getActiveCalory();
        String exerciseHabit = info.getExerciseHabit();


        int ss =0; //걸음점수
        int as =0; //칼로리 점수
        int es =0; //운동습관 점수

        if(stepCounts >= 8000){
            ss =50;
        } else if(stepCounts >= 6000){
            ss =40;
        } else if(stepCounts >= 4000){
            ss =20;
        } else if(stepCounts >= 2000){
            ss =10;
        } else if(stepCounts >= 1000){
            ss =5;
        } else{
            ss=0;
        }

        if(user.getSex()== Sex.MALE){
            if(activeCalories>= 300){
                as =20;
            } else if(activeCalories >=250){
                as =15;
            } else if(activeCalories >=200){
                as =10;
            } else if(activeCalories >=150){
                as =5;
            } else{
                as =0;
            }
        } else{ //여자라면
            if(activeCalories>= 250){
                as =20;
            } else if(activeCalories >=200){
                as =15;
            } else if(activeCalories >=150){
                as =10;
            } else if(activeCalories >=100){
                as =5;
            } else{
                as =0;
            }
        }

        if(exerciseHabit.equals("거의 매일")){
            es =50;
        } else if(exerciseHabit.equals("주 3회 이상")){
            es =40;
        } else if(exerciseHabit.equals("주 1-2회")){
            es =30;
        } else if(exerciseHabit.equals("월 1-2회")){
            es =20;
        } else{
            es =0;
        }

    return ss+as+es;
    }


//  2. 신체점수 도출 메서드
//    심박수 점수 40점 + 수면 점수 40점 + bmi점수 20점
  public static int whatsMybodyScore(HealthData healthData, UserDetailHealthInfo info, User user){
       int heartBeats =healthData.getHeartbeat(); // 평균심박
       int totalSleepMinutes = healthData.getTotalSleepMinutes(); //총 수면시간
       int remSleepMinutes = healthData.getRemSleepMinutes(); //렘 수면시간
       int lightSleepMinutes =healthData.getLightSleepMinutes(); //얕은 수면시간
       int deepSleepMinutes =healthData.getDeepSleepMinutes(); // 깊은 수면시간
        Map<String, Object> mybmi = user.makingBmi();
        Object bmioj = mybmi.get("category");
        String bmi = bmioj.toString();
       int hs = 0; //심박점수
       int shs = 0; //수면 시간 점수(20)
       int sss = 0; //수면 단계 점수(20)
       int bs = 0; //체질량지수 점수

      //정상심박은 60~100bpm. 노년층의 경우 60~80이 이상적
       if(heartBeats >=60 && heartBeats <85){
           hs = 40;
       } else if(heartBeats >=85 && heartBeats<= 100){
           hs =20;
       } else{
           hs = 10;
       }

       //7시간(420분)~9시간(540분) 정상
       if(totalSleepMinutes >=420 && totalSleepMinutes<600){
           shs =20;
       } else if(totalSleepMinutes >=360 && totalSleepMinutes<420){
           shs =15;
       } else if(totalSleepMinutes >=300 && totalSleepMinutes<360){
           shs =10;
       } else{ //5시간 미만으로 자면 0점 , 10시간 이상 자도 과도수면 0점
           shs =0;
       }

       //깊은 수면이 전체 수면의 13~23%가 이상적. REM수면이 20~25%가 적정
       double deepRatio = (double) deepSleepMinutes /totalSleepMinutes;
       double remRatio = (double) remSleepMinutes / totalSleepMinutes;

       if (deepRatio>=0.13 && deepRatio <=0.23 && remRatio>=0.2 && remRatio <=0.25){
           sss = 20;
       } else if((deepRatio>=0.1 && deepRatio<=0.25) || (remRatio>=0.15 && remRatio<=0.3)){
            sss =15;
      } else{
           sss =10;
       }

       switch (bmi){
           case "정상체중" :
               bs = 20;
               break;

           case "과체중" :
               bs =15;
               break;

           case "저체중" :
               bs =10;
               break;

           case "비만" :
               bs = 5;
               break;
       }

       return hs+shs+sss+bs;
  }


//    3.habitScore 도출 메서드
    //음주습관 30점(as) + 흡연습관 40점(ss) + 기저질환30점(ds)
    public static int whatsMyHabitScore(UserDetailHealthInfo info){
        String alcoholHabit = info.getAlcoholHabit();
        String smokingHabit = info.getSmokingHabit();
        String disease = info.getDisease();

        int as =0;
        int ss =0;
        int ds =0;

        if(alcoholHabit.equals("전혀 안함")){
            as = 30;
        } else if(alcoholHabit.equals("월 1-2회")){
            as = 25;
        } else if(alcoholHabit.equals("주 1-2회")){
            as = 10;
        }else{ //주 3회이상
            as = 0;
        }

        if(smokingHabit.equals("전혀 안함")){
            ss = 40;
        } else if(smokingHabit.equals("하루 5개피 이하")){
            ss = 20;
        } else if(smokingHabit.equals("하루 반갑 이상")){
            ss = 10;
        } else {
            ss = 0;
        }

        if(disease.equals("없음")){
            ds = 30;
        } else{
            int count = disease.split(",").length; //질환의 개수
            switch (count){
                case 1:
                    ds =25;
                    break;
                case 2:
                    ds =15;
                    break;
                case 3:
                    ds = 5;
                    break;
                default:
                    ds =0;
                    break;
            }
        }
        return as+ss+ds;
    }

//  찐 헬스점수 도출 메서드

  public static int totalScore(int activityScore, int bodyScore, int habitScore){
        return (int)Math.round(activityScore*0.4 + bodyScore *0.3 + habitScore *0.3);
  }

}
