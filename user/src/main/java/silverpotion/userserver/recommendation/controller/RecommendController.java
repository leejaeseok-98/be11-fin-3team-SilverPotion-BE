package silverpotion.userserver.recommendation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.userserver.common.dto.CommonDto;
import silverpotion.userserver.recommendation.service.RecommendService;

@RestController
@RequestMapping("/silverpotion/recommend")
public class RecommendController {

    private final RecommendService recommendService;


    public RecommendController(RecommendService recommendService) {
        this.recommendService = recommendService;
    }

//    사용자에게 맞는 소모임 5개 추천
    @GetMapping("/fivegatherings")
    public ResponseEntity<?> recommendGatherings(@RequestHeader("X-User-LoginId") String loginId){
//        여기서 소모임 리스트 5개를 리턴해야함(일단은 유저벡터만드는 로직으로)
        recommendService.recommendGatherings(loginId);
        return new ResponseEntity<>(new CommonDto(HttpStatus.OK.value(), "success","success"),HttpStatus.OK);
    }



}
