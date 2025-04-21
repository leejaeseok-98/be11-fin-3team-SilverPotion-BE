package silverpotion.userserver.batch.monthly;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import silverpotion.userserver.healthData.domain.AverageData;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@Component
public class MonthlyHealthDataProcessor implements ItemProcessor<User, HealthData> {

    private final HealthDataRepository healthDataRepository;

    public MonthlyHealthDataProcessor(HealthDataRepository healthDataRepository) {
        this.healthDataRepository = healthDataRepository;
    }


    @Override
    //ItemReader가 넘긴 유저 한 명에 대해 실행되고, 이 안에서 평균 계산
    public HealthData process(User user) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);//오늘로부터 1달을 뺀 날이 있는 달의 첫번째 날. 즉 몇월 1일
        LocalDate endDate = today.minusDays(1); //오늘 하루전. (어차피 매달 1일에 실행될 것이기때문에 1일에 하루전을 빼면 전달의 마지막날이 되니까)

        List<HealthData> weekData = healthDataRepository.findByUserIdAndCreatedDateBetweenAndDataType(user.getId(), startDate, endDate, DataType.DAY); //전달의 1일부터 전달의 마지막날까지의 데이터
        if(weekData.isEmpty()) {
            return null; // 스프링 배치는 null 을 무시하고 넘어가기 때문에 nullPointerException과 같은 에러가 발생하지 않는다
        }

        AverageData averageData = AverageData.makeAvg(weekData); //내가 만든 평균 내는 클래스
        HealthData monthAvg =
                HealthData.builder().dataType(DataType.MONTHAVG).step(averageData.getAvgStep()).heartbeat(averageData.getAvgHeartBeat())
                        .distance(averageData.getAvgDistancd()).calory(averageData.getAvgCalory()).activeCalory(averageData.getAvgActiveCalory())
                        .totalSleepMinutes(averageData.getAvgTotalSleepMinutes()).deepSleepMinutes(averageData.getAvgDeepSleepMinutes()).lightSleepMinutes(averageData.getAvgLightSleepMinutes())
                        .remSleepMinutes(averageData.getAvgRemSleepMinutes())
                        .user(user).createdDate(today).build();
        return monthAvg;
    }
}
