package silverpotion.userserver.healthData.reopisitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import silverpotion.userserver.healthData.domain.DataType;
import silverpotion.userserver.healthData.domain.HealthData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HealthDataRepository extends JpaRepository<HealthData,Long> {

    Optional<HealthData> findByUserIdAndCreatedDateAndDataType(Long userId, LocalDate today,DataType dataType);
    //어떤 유저의 start날짜부터 end데이터까지
    List<HealthData> findByUserIdAndCreatedDateBetweenAndDataType(Long userId, LocalDate start, LocalDate end, DataType dataType);


}