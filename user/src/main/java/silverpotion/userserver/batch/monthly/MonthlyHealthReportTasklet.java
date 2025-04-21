package silverpotion.userserver.batch.monthly;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import silverpotion.userserver.openAi.service.HealthReportService;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.util.List;

@Component
public class MonthlyHealthReportTasklet implements Tasklet {

    private final HealthReportService healthReportService;
    private final UserRepository userRepository;

    public MonthlyHealthReportTasklet(HealthReportService healthReportService, UserRepository userRepository) {
        this.healthReportService = healthReportService;
        this.userRepository = userRepository;
    }


    @Override
    //여기가 Tasklet이 수행되는 실제 비즈니스 로직
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        List<User> users = userRepository.findAll();
        for(User u: users){
            try {
                healthReportService.monthlyReportMake(u.getLoginId()).block(); //Mono는 비동기이기 때문에 원래는 subscribe를 호출해야 실제 실행됨. subscribe()를 붙이지 않으면 아무것도 안일어남, 근데 여기서는 헬스데이터 저장되기전에 이 태스트릿이 실행되어 기다리라는 의미에서 block

            } catch (EntityNotFoundException e){
                System.out.println("리포트 생략"+ u.getLoginId() + "헬스데이터" + u.getMyHealthData().size());

            } catch(Exception e){
                System.out.println("다른 예외");
            }
        }
        return RepeatStatus.FINISHED; //이 Tasklet은 끝났다는 의미.
    }
}
