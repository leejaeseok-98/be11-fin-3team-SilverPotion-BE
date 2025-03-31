package silverpotion.userserver.healthData.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.careRelation.domain.CareRelation;
import silverpotion.userserver.healthData.domain.HealthData;
import silverpotion.userserver.healthData.domain.HeartRateData;
import silverpotion.userserver.healthData.dtos.HealthAvgDataDto;
import silverpotion.userserver.healthData.dtos.HealthDataListDto;
import silverpotion.userserver.healthData.dtos.HealthDataSpecificDateDto;
import silverpotion.userserver.healthData.dtos.HealthSyncDto;
import silverpotion.userserver.healthData.reopisitory.HealthDataRepository;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HealthDataService {
    private final HealthDataRepository healthDataRepository;
    private final UserRepository userRepository;


    public HealthDataService(HealthDataRepository healthDataRepository, UserRepository userRepository) {
        this.healthDataRepository = healthDataRepository;
        this.userRepository = userRepository;
    }

    //  1.앱으로부터 데이터를 받아와 HealthData 생성
    public void save(HealthSyncDto dto,String loinId){
        User user = userRepository.findByLoginIdAndDelYN(loinId, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        //오늘 날짜
        LocalDate today =LocalDate.now();

        //평균 심박수
        List<HeartRateData>beatList = dto.getHeartRateData();
        int sum = 0;
        for(HeartRateData h : beatList){
            sum +=(int)h.getBpm();
        }
        int averageBpm = sum / beatList.size();
        //엔티티 객체 생성
        HealthData newData = dto.toEntityFromSync(averageBpm,user,today);
        Optional<HealthData> todayHealthData = healthDataRepository.findByUserIdAndCreatedDate(user.getId(), today);
        if(todayHealthData.isPresent()){ // 이미 오늘 날짜에 생성된 헬스데이터가 있다면 기존의 것을 지우고 최근 엔티티 객체를 새로 저장
            healthDataRepository.delete(todayHealthData.get());
            healthDataRepository.save(dto.toEntityFromSync(averageBpm,user,today));
            //유저의 헬스데이터 리스트에서 오늘 날짜의 데이터를 찾아 제거
        user.getMyHealthData().removeIf(data -> data.getCreatedDate().equals(today));
        user.getMyHealthData().add(newData);
        } else{ // 오늘날짜에 생성된 헬스데이터가 없다면 바로 저장
          HealthData data = healthDataRepository.save(dto.toEntityFromSync(averageBpm,user,today));
          user.getMyHealthData().add(data);

        }
    }

    //  2. 헬스데이터 오늘꺼 조회
    public HealthDataListDto todayData(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        LocalDate today = LocalDate.now();
        return healthDataRepository.findByUserIdAndCreatedDate(user.getId(), today).orElseThrow(()->new EntityNotFoundException("금일 기록이 없습니다")).toListDtoFromEntity();
    }

    //    3. 헬스데이터 특정날짜 조회
    public HealthDataListDto specificDateData(HealthDataSpecificDateDto dto, String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        LocalDate specificDate = LocalDate.parse(dto.getSpecificDate());
        return healthDataRepository.findByUserIdAndCreatedDate(user.getId(), specificDate).orElseThrow(()->new EntityNotFoundException("해당날짜의 기록이 없습니다")).toListDtoFromEntity();
    }

    //   4.헬스데이터 과거 일주일치 평균 조회
    public HealthAvgDataDto weeklyAvgHealthData(String loginId){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        LocalDate today = LocalDate.now(); //오늘 날짜
        LocalDate thisMonday = today.with(DayOfWeek.MONDAY);//이번주 월요일
        LocalDate lastMonday = thisMonday.minusWeeks(1);//지난주 월요일
        LocalDate lastSunday = thisMonday.minusDays(1);//지난주 일요일
        List<HealthData> weekList = healthDataRepository.findByUserIdAndCreatedDateBetween(user.getId(),lastMonday,lastSunday);
        //리스트에서 각각의 요소를 평균냄. (null값이나 0을 제외하려면 필터설정을 아래와 같이 걸면 됨)
        // double avgStep = weekList.stream().filter(d->d.getStep() !=null).mapToInt(HealthData::getStep).average().orElse(0);
        //       double avgHeartBeat = weekList.stream().filter(d->d.getHeartbeat() !=0).mapToInt(HealthData::getHeartbeat).average().orElse(0);
        int avgStep = (int)weekList.stream().mapToInt(HealthData::getStep).average().orElse(0);
        int avgHeartBeat = (int)weekList.stream().mapToInt(HealthData::getHeartbeat).average().orElse(0);
        int avgDistancd = (int)weekList.stream().mapToDouble(HealthData::getDistance).average().orElse(0);
        int avgCalory = (int)weekList.stream().mapToDouble(HealthData::getCalory).average().orElse(0);
        int avgActiveCalory = (int)weekList.stream().mapToDouble(HealthData::getActiveCalory).average().orElse(0);

        return HealthAvgDataDto.builder().step(avgStep).heartbeat(avgHeartBeat).distance(avgDistancd)
                .calory(avgCalory).activeCalory(avgActiveCalory)
                .build();
    }

    //   5.헬스데이터 월별 평균 조회(이번달)
    public HealthAvgDataDto monthlyAvgHealthData(String loginId) {
        User user = userRepository.findByLoginIdAndDelYN(loginId, DelYN.N).orElseThrow(() -> new EntityNotFoundException("없는 회원입니다"));
        LocalDate today = LocalDate.now(); //오늘 날짜
        LocalDate firstDayOfMonth = today.withDayOfMonth(1); //이번달 시작 날짜
        List<HealthData> weekList = healthDataRepository.findByUserIdAndCreatedDateBetween(user.getId(), firstDayOfMonth, today);
        //리스트에서 각각의 요소를 평균냄. (null값이나 0을 제외하려면 필터설정을 아래와 같이 걸면 됨)
        int avgStep = (int)weekList.stream().mapToInt(HealthData::getStep).average().orElse(0);
        int avgHeartBeat = (int)weekList.stream().mapToInt(HealthData::getHeartbeat).average().orElse(0);
        int avgDistancd = (int)weekList.stream().mapToDouble(HealthData::getDistance).average().orElse(0);
        int avgCalory =(int)weekList.stream().mapToDouble(HealthData::getCalory).average().orElse(0);
        int avgActiveCalory = (int)weekList.stream().mapToDouble(HealthData::getActiveCalory).average().orElse(0);

        return HealthAvgDataDto.builder().step(avgStep).heartbeat(avgHeartBeat).distance(avgDistancd)
                .calory(avgCalory).activeCalory(avgActiveCalory)
                .build();
    }

//    6.나의 피보호자 헬스데이터 조회(피보호자 목록 탭 조회할 때 백엔드로부터 피보호자 id를 받게되므로 프론트는 그 id를 가지고 피보호자 데이터 조회하도록 설계)
     public HealthDataListDto mydependentData(String loginId, Long id){
        User user = userRepository.findByLoginIdAndDelYN(loginId,DelYN.N).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다"));
        User dependentUser = userRepository.findByIdAndDelYN(id,DelYN.N).orElseThrow(()-> new EntityNotFoundException("없는 회원입니다"));

        List<CareRelation> relationAsProtector = user.getAsProtectors();
         HealthData targetData = new HealthData();
        for(CareRelation c: relationAsProtector){
           if(c.getDependent().getId().equals(id)){
               List<HealthData> dependentDataList = dependentUser.getMyHealthData();
               LocalDate today = LocalDate.now();
               for(HealthData h : dependentDataList){
                   if(h.getCreatedDate().equals(today)){
                       targetData = h;
                   }
               }

           } else{
               throw new IllegalArgumentException("피보호자로 등록되지 않은 회원입니다");
           }
        }
         return targetData.toListDtoFromEntity();

     }

}