package silverpotion.userserver.healthData.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.healthData.domain.AverageData;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class HealthDataBatchService {
    private final HealthDataRepository healthDataRepository;
    private final UserRepository userRepository;

    public HealthDataBatchService(HealthDataRepository healthDataRepository, UserRepository userRepository) {
        this.healthDataRepository = healthDataRepository;
        this.userRepository = userRepository;
    }



//    @Scheduled(fixedRate = 600000) //10분마다 실행(테스트용)
    @Scheduled(cron = "0 0 0 * * MON") //매주 월요일 0시마다 실행
    public void createAllUsersWeeklyAvgHealthData(){
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();
        //이번주 월요일의 -1주 = 전주 월요일
        LocalDate startDate = today.with(DayOfWeek.MONDAY).minusWeeks(1);
        //이번주 일요일의 -1주 = 전주 일요일
        LocalDate endDate = today.with(DayOfWeek.SUNDAY).minusWeeks(1);

        for(User u: users){
            List<HealthData>dataForWeekAvg = healthDataRepository.findByUserIdAndCreatedDateBetweenAndDataType(u.getId(),startDate,endDate,DataType.DAY);
            AverageData averageData = AverageData.makeAvg(dataForWeekAvg);
            HealthData weekAvg =
            HealthData.builder().dataType(DataType.WEEKAVG).step(averageData.getAvgStep()).heartbeat(averageData.getAvgHeartBeat())
                    .distance(averageData.getAvgDistancd()).calory(averageData.getAvgCalory()).activeCalory(averageData.getAvgActiveCalory())
                    .user(u).createdDate(today).build();
            healthDataRepository.save(weekAvg);
        }
    }
//        @Scheduled(fixedRate = 600000) //10분마다 실행(테스트용)
    @Scheduled(cron = "0 0 0 1 * *") //매달 1일마다
    public void createAllUsersMonthlyAvgHealthData(){
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();
        //이번주 월요일의 -1주 = 전주 월요일
        LocalDate startDate = today.minusMonths(1).withDayOfMonth(1);//오늘로부터 1달을 뺀 날이 있는 달의 첫번째 날. 즉 몇월 1일
        //이번주 일요일의 -1주 = 전주 일요일
        LocalDate endDate = today.minusDays(1);

        for(User u: users){
            List<HealthData>dataForMonthAvg = healthDataRepository.findByUserIdAndCreatedDateBetweenAndDataType(u.getId(),startDate,endDate,DataType.WEEKAVG);
            AverageData averageData = AverageData.makeAvg(dataForMonthAvg);
            HealthData monthlyAvg =
                    HealthData.builder().dataType(DataType.MONTHAVG).step(averageData.getAvgStep()).heartbeat(averageData.getAvgHeartBeat())
                            .distance(averageData.getAvgDistancd()).calory(averageData.getAvgCalory()).activeCalory(averageData.getAvgActiveCalory())
                            .user(u).createdDate(today).build();
            healthDataRepository.save(monthlyAvg);
        }
    }

}
