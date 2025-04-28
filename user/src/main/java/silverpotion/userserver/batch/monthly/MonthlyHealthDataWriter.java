package silverpotion.userserver.batch.monthly;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;

@Component
public class MonthlyHealthDataWriter implements ItemWriter<HealthData> {

    private final HealthDataRepository healthDataRepository;

    public MonthlyHealthDataWriter(HealthDataRepository healthDataRepository) {
        this.healthDataRepository = healthDataRepository;
    }

    @Override
    //청크는 리스트 같은 하나의 묶음. 우리가 config에서 설정한 크기. 여기선 100개 단위로 처리. 즉 IteamReader->Processor해서 100번 작업하여 100개의 HealthData가 만들어지면 하나의 청크가 묶인다.
    //하나의 청크가 생기면 자동으로 write()가 호출되어 100개의 HealthData가 자동으로 db에 저장된다.

    public void write(Chunk<? extends HealthData> chunk) throws Exception {
            healthDataRepository.saveAll(chunk);
            healthDataRepository.flush(); //헬스데이터 만드는 스텝다음에 그 헬스데이터를 기반으로 헬스리포트가 생성되는데 스텝간 순서는 맞을 지라도 jpa가 db에 아직 인서트 하지 않아서 다음 헬스리포트가 생성이 안될 수 있어서 여기서 flush
    }
}
