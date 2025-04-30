package silverpotion.userserver.healthScore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.userserver.healthScore.dtos.HealthScoreMakeReqDto;
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
    public ResponseEntity<?> makingHealthScore(@RequestHeader("X-User-loginId")String loginId, HealthScoreMakeReqDto dto){
                healthScoreService.makingHealthScore(loginId, dto);
    }
}
