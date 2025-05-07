package silverpotion.userserver.healthScore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.healthScore.domain.HealthScore;
import silverpotion.userserver.healthScore.dtos.HealthScoreMakeReqDto;
import silverpotion.userserver.healthScore.dtos.HealthScoreResDto;
import silverpotion.userserver.healthScore.service.HealthScoreService;

@RestController
@RequestMapping("/silverpotion/healthscore")
public class HealthScoreController {

    private final HealthScoreService healthScoreService;

    public HealthScoreController(HealthScoreService healthScoreService) {
        this.healthScoreService = healthScoreService;
    }

//  헬스점수 생성 및 조회
    @PostMapping("/create")
    public ResponseEntity<?> makingHealthScore(@RequestHeader("X-User-loginId")String loginId,@RequestBody HealthScoreMakeReqDto dto){
            HealthScoreResDto healthScoreResDto = healthScoreService.makingHealthScore(loginId, dto);
            return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(),"success",healthScoreResDto),HttpStatus.OK);

    }
}
