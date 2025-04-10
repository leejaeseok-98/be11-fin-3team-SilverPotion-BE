package silverpotion.userserver.fireBase.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.userserver.fireBase.domain.TokenRequest;
import silverpotion.userserver.fireBase.service.FireBaseService;


@RestController
@RequestMapping("silverpotion/firebase")
public class FireBaseController {
    private final FireBaseService fireBaseService;

    public FireBaseController(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
    }

//    1.앱으로부터 파이어베이스 토큰 전송 받는 url(해당 토큰을 해당 유저에 저장)
    @PostMapping("/token")
    public void saveToken(@RequestBody TokenRequest request){
        fireBaseService.saveTokenToUser(request);
    }




}
