package silverpotion.userserver.userDetailHealthInfo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.userDetailHealthInfo.dtos.UserDetailHealthInfoCreateReqDto;
import silverpotion.userserver.userDetailHealthInfo.service.UserDetailHealthInfoService;

import java.util.Map;

@RestController
@RequestMapping("silverpotion/detailhealthinfo")
public class UserDetailHealthInfoController {

    private final UserDetailHealthInfoService userDetailHealthInfoService;

    public UserDetailHealthInfoController(UserDetailHealthInfoService userDetailHealthInfoService) {
        this.userDetailHealthInfoService = userDetailHealthInfoService;
    }


//    1. 유저상세건강정보 추가(생성)
    @PostMapping("/create")
    public ResponseEntity<?> detailInfoAdd(@RequestHeader("X-User-loginId")String loginId,@RequestBody UserDetailHealthInfoCreateReqDto dto){
         userDetailHealthInfoService.detailInfoAdd(loginId, dto);
         return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "sucess","sucess"),HttpStatus.OK);
        }

//  2. BMI 지수 조회
    @GetMapping("/bmicheck")
    public ResponseEntity<?> bmiCheck(@RequestHeader("X-User-loginId")String loginId){
        Map<Double,String> myBmi = userDetailHealthInfoService.bmiCheck(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"sucess",myBmi),HttpStatus.OK);
    }








    }







