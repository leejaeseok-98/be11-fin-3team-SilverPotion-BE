package silverpotion.userserver.healthData.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.healthData.dtos.HealthAvgDataDto;
import silverpotion.userserver.healthData.dtos.HealthDataListDto;
import silverpotion.userserver.healthData.dtos.HealthDataSpecificDateDto;
import silverpotion.userserver.healthData.dtos.HealthSyncDto;
import silverpotion.userserver.healthData.service.HealthDataService;

@RestController
@RequestMapping("/silverpotion/health")
public class HealthDataController {

    private final HealthDataService healthDataService;


    public HealthDataController(HealthDataService healthDataService) {
        this.healthDataService = healthDataService;
    }

    //    1.앱으로부터 데이터 받아오는 api
    @PostMapping("/fromPhone")
    public void receiveData(@RequestBody HealthSyncDto dto, @RequestHeader("X-User-Id") String loginId) {
        healthDataService.save(dto, loginId);
    }


    //    2.헬스데이터 오늘꺼 조회
    @GetMapping("/dataList")
    public ResponseEntity<?> myHealthData(@RequestHeader("X-User-Id") String loginId) {
        HealthDataListDto todayData = healthDataService.todayData(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "today's healthData is uploaded well", todayData), HttpStatus.OK);
    }

    //    3.헬스데이터 특정날짜 조회
    @PostMapping("/specificDataList")
    public ResponseEntity<?> mySpecificHealthData(@RequestBody HealthDataSpecificDateDto dto, @RequestHeader("X-User-Id") String loginId) {
        HealthDataListDto specificData = healthDataService.specificDateData(dto,loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "today's healthData is uploaded well", specificData), HttpStatus.OK);
    }

    //    4.헬스데이터 지난주 평균 조회(지난주)
    @GetMapping("/weeklyavg")
    public ResponseEntity<?> weeklyAvgHealthData(@RequestHeader("X-User-Id")String loginId){
        HealthAvgDataDto weeklyAvg = healthDataService.weeklyAvgHealthData(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "weekly AvgHealthData is uploaded well",weeklyAvg),HttpStatus.OK);
    }

    //   5.헬스데이터 이번달 평균 조회
    @GetMapping("/monthlyavg")
    public ResponseEntity<?> monthlyAvgHealthData(@RequestHeader("X-User-Id")String loginId){
        HealthAvgDataDto monthlyAvg = healthDataService.monthlyAvgHealthData(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "monthly AvgHealthData is uploaded well",monthlyAvg),HttpStatus.OK);
    }

    //  6.내 피보호자 헬스데이터 조회
//    @GetMapping("/yourHealthData/{id}")
//    public ResponseEntity<?> mydependentData(@RequestHeader("X-User_Id")String loginId, @PathVariable Long id){
//        HealthDataListDto depentData = healthDataService.mydependentData(loginId, id);

    }










