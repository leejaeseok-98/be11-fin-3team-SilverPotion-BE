package silverpotion.userserver.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import silverpotion.userserver.healthData.domain.AverageData;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.user.domain.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
public class WeeklyHealthDataProcessor implements ItemProcessor<User, HealthData> {

    private final HealthDataRepository healthDataRepository;

    public WeeklyHealthDataProcessor(HealthDataRepository healthDataRepository) {
        this.healthDataRepository = healthDataRepository;
    }


    @Override
    //ItemReader가 넘긴 유저 한 명에 대해 실행되고, 이 안에서 평균 계산
    public HealthData process(User user) throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.with(DayOfWeek.MONDAY).minusWeeks(1); //지난 주 월요일
        LocalDate endDate = today.with(DayOfWeek.SUNDAY).minusWeeks(1); //지난 주 일요일

        List<HealthData> weekData = healthDataRepository.findByUserIdAndCreatedDateBetweenAndDataType(user.getId(), startDate, endDate, DataType.DAY); //지난 주 월요일~일요일까지의 일간 데이터
        if(weekData.isEmpty()) {
            return null; // 스프링 배치는 null 을 무시하고 넘어가기 때문에 nullPointerException과 같은 에러가 발생하지 않는다
        }

        AverageData averageData = AverageData.makeAvg(weekData); //내가 만든 평균 내는 클래스
        HealthData weekAvg =
                HealthData.builder().dataType(DataType.WEEKAVG).step(averageData.getAvgStep()).heartbeat(averageData.getAvgHeartBeat())
                        .distance(averageData.getAvgDistancd()).calory(averageData.getAvgCalory()).activeCalory(averageData.getAvgActiveCalory())
                        .totalSleepMinutes(averageData.getAvgTotalSleepMinutes()).deepSleepMinutes(averageData.getAvgDeepSleepMinutes()).lightSleepMinutes(averageData.getAvgLightSleepMinutes())
                        .remSleepMinutes(averageData.getAvgRemSleepMinutes())
                        .user(user).createdDate(today).build();
        user.getMyHealthData().add(weekAvg);
        return weekAvg;
    }
}
