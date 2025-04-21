package silverpotion.userserver.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.user.domain.User;

@Configuration
public class SpringBatchConfig {

    private final JobRepository jobRepository; //배치 작업의 실행이력을 db에 저장하는 역할.job이 언제 실행되었는지, 성공했는지 실패했는지 등
    private final PlatformTransactionManager transactionManager; //배치는 db작업을 묶어서 실행하는 경우가 많음. 이걸 트랜잭션 단위로 관리하기 위해 필요한 객체. 얘가 있어야 작업도중 실패하면 롤백이 가능
    private final WeeklyHealthDataReader weeklyHealthDataReader;
    private final WeeklyHealthDataProcessor weeklyHealthDataProcessor;
    private final WeeklyHealthDataWriter weeklyHealthDataWriter;
    private final WeeklyHealthReportTasklet weeklyHealthReportTasklet;
    private final MonthlyHealthDataReader monthlyHealthDataReader;
    private final MonthlyHealthDataProcessor monthlyHealthDataProcessor;
    private final MonthlyHealthDataWriter monthlyHealthDataWriter;
    private final MonthlyHealthReportTasklet monthlyHealthReportTasklet;

    public SpringBatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, WeeklyHealthDataReader weeklyHealthDataReader, WeeklyHealthDataProcessor weeklyHealthDataProcessor, WeeklyHealthDataWriter weeklyHealthDataWriter, WeeklyHealthReportTasklet weeklyHealthReportTasklet, MonthlyHealthDataReader monthlyHealthDataReader, MonthlyHealthDataProcessor monthlyHealthDataProcessor, MonthlyHealthDataWriter monthlyHealthDataWriter, MonthlyHealthReportTasklet monthlyHealthReportTasklet) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.weeklyHealthDataReader = weeklyHealthDataReader;
        this.weeklyHealthDataProcessor = weeklyHealthDataProcessor;
        this.weeklyHealthDataWriter = weeklyHealthDataWriter;
        this.weeklyHealthReportTasklet = weeklyHealthReportTasklet;
        this.monthlyHealthDataReader = monthlyHealthDataReader;
        this.monthlyHealthDataProcessor = monthlyHealthDataProcessor;
        this.monthlyHealthDataWriter = monthlyHealthDataWriter;
        this.monthlyHealthReportTasklet = monthlyHealthReportTasklet;
    }


    @Bean
    @Qualifier("weeklyAverageHealthJob")
//    실제로 실행할 배치작업을 만듬.Job은 하나의 배치 작업 단위. ex.모든 사용자 주간 건강데이터 평균 내기
    public Job weeklyAverageHealthJob(){
        return new JobBuilder("weeklyAverageHealthJob", jobRepository) //JobBuilder를 job을 만드는 도구로 인자로 이 job의 이름과 jobRepository를 받음. 여기서 바로 job의 이름을 설정하는 것이기도 함
                .start(weeklyAverageStep()) // 이 job을 시작 할 때 어떤 step을 실행할 지 지정하는 것
                .build();
    }

    @Bean
//   위에서 지정한 Step을 정의하는 부분(Step은 실제로 데이터를 읽고, 가공하고, 저장하는 작업단위)
    public Step weeklyAverageStep(){
        return new StepBuilder("weeklyAverageStep",jobRepository) //StepBuilder는 Step을 만들기 위한 Builder 마찬가지로 이름과 jobRepository를 인자로 받으며, 바로 step의 이름을 설정
//<InputType,OutputType>chunk(청크크기,트랜잭션매니저) 인풋타입은 읽어들인 데이터로 ItemReader의 출력타입이고 아웃풋타입은 처리된 후의 데이터로(ItemWriter의 입력 타입이다). 즉 이 스텝은 User객체를 읽고->HealthData로 바꾸고 DB에 저장한다는 흐름
                .<User, HealthData>chunk(100,transactionManager) // 이 스텝이 chunk기반으로 처리하겠다.(이외에도 tasklet도 있음), 100개단위의 트랜잭션 단위
                .reader(weeklyHealthDataReader) //이 스텝이 사용할 ItemReader 입력 데이터를 읽는 역할(여기서는 모든 User를 불러옴)
                .processor(weeklyHealthDataProcessor) //이 스탭이 사용할 ItemProcessor 읽어온 데이터를 가공하는 역할(여기서는 해당 유저의 주간 평균 HealthData생성/ 로직)
                .writer(weeklyHealthDataWriter) //이 스탭이 사용할 ItemWriter 처리된 결과를 저장하는 역할(여기서는 DB에 HealthData저장)
                .build();
    }


    @Bean
    @Qualifier("weeklyHealthReportJob")
    public Job weeklyHealthReportJob() {
        return new JobBuilder("weeklyHealthReportJob", jobRepository)
                .start(weeklyHealthReportStep()) // 하나의 step만 있는 job
                .build();
    }

    @Bean
    public Step weeklyHealthReportStep(){
        return new StepBuilder("weeklyHealthReportStep",jobRepository)
                .tasklet(weeklyHealthReportTasklet,transactionManager)
                .build();
    }


    @Bean
    @Qualifier("monthlyAverageHealthJob")
    public Job monthlyAverageHealthJob(){
        return new JobBuilder("monthlyAverageHealthJob",jobRepository)
                .start(monthlyAverageStep())
                .next(monthlyHealthReportStep())
                .build();
    }

    @Bean
    public Step monthlyAverageStep(){
        return new StepBuilder("monthlyStep",jobRepository)
                .<User, HealthData>chunk(100,transactionManager)
                .reader(monthlyHealthDataReader)
                .processor(monthlyHealthDataProcessor)
                .writer(monthlyHealthDataWriter)
                .build();
    }

    @Bean
    public Step monthlyHealthReportStep(){
        return new StepBuilder("monthyHealthReportStep",jobRepository)
                .tasklet(monthlyHealthReportTasklet,transactionManager)
                .build();
    }



}
