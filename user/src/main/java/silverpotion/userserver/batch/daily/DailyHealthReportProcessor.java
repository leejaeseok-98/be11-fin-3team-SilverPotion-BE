package silverpotion.userserver.batch.daily;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import silverpotion.userserver.healthData.domain.AverageData;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.openAi.domain.HealthReport;
import silverpotion.userserver.openAi.service.HealthReportService;
import silverpotion.userserver.user.domain.User;

import java.time.LocalDate;
import java.util.List;

@Component
public class DailyHealthReportProcessor implements ItemProcessor<User, HealthReport> {

    private final HealthReportService healthReportService;

    public DailyHealthReportProcessor(HealthReportService healthReportService) {
        this.healthReportService = healthReportService;
    }


    @Override
    //ItemReader가 넘긴 유저 한 명에 대해 실행되고, 이 안에서 평균 계산
    public HealthReport process(User user) throws Exception {
      return healthReportService.dailyReportMake(user.getLoginId()).block()  ;

    }
}
