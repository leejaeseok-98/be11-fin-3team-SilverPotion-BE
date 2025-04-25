package silverpotion.userserver.batch.daily;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.openAi.domain.HealthReport;
import silverpotion.userserver.openAi.repository.HealthReportRepository;

@Component
public class DailyHealthReportWriter implements ItemWriter<HealthReport> {

    private final HealthReportRepository healthReportRepository;

    public DailyHealthReportWriter(HealthReportRepository healthReportRepository) {
        this.healthReportRepository = healthReportRepository;
    }

    @Override
    //청크는 리스트 같은 하나의 묶음. 우리가 config에서 설정한 크기. 여기선 100개 단위로 처리. 즉 IteamReader->Processor해서 100번 작업하여 100개의 HealthData가 만들어지면 하나의 청크가 묶인다.
    //하나의 청크가 생기면 자동으로 write()가 호출되어 100개의 HealthData가 자동으로 db에 저장된다.
    @Transactional(propagation = Propagation.REQUIRES_NEW) //무조건 새트랜잭션 시작하는 어노테이션 이 어노테이션으로 인해 processor에서 청크중에 에러가 나서 db에 저장되었던게 롤백되는 걸 막아줌
    public void write(Chunk<? extends HealthReport> chunk) throws Exception {
        healthReportRepository.saveAll(chunk);
        healthReportRepository.flush(); // 현재 전체 job에서 이 주간데이터가 다 만들어져도 db에는 바로 저장이 안되서 주간데이터를 기반으로 주간리포트를 만드는 다음 스탭에서 에러가 남. 따라서 flush 는 db로 즉시 쿼리를 날리게 함
    }
}
