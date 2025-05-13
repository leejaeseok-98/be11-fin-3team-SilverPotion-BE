package silverpotion.userserver.common.smsController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import silverpotion.userserver.common.dto.AutoCodeDto;
import silverpotion.userserver.common.smsService.SmsService;

@RestController
@RequestMapping("/silverpotion/sms")
@PreAuthorize("permitAll()")
public class SmsController {
    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }

//인증번호 전송 API(휴대번호 입력 시 전송)
    @PostMapping("/send-auth")
    public String sendAuthCode(@RequestBody AutoCodeDto dto){
        System.out.println(dto.getPhoneNumber());
        return smsService.sendAuthCode(dto.getPhoneNumber());
    }

    @PostMapping("/verify-auth")
    public boolean verifyAuthCode(@RequestBody AutoCodeDto dto){
        return smsService.verifyAuthCode(dto.getPhoneNumber(), dto.getInputCode());
    }
}
