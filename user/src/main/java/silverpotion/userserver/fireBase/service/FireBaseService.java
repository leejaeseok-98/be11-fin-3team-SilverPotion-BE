package silverpotion.userserver.fireBase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import silverpotion.userserver.fireBase.domain.TokenRequest;
import silverpotion.userserver.user.domain.DelYN;
import silverpotion.userserver.user.domain.User;
import silverpotion.userserver.user.repository.UserRepository;
import silverpotion.userserver.user.service.UserService;

@Service
@Transactional
public class FireBaseService {
    private final UserRepository userRepository;

    public FireBaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//  1.앱으로부터 파이어베이스 토큰 전송받는 url
    public void saveTokenToUser(TokenRequest tokenRequest){
        String phNumber = tokenRequest.getPhoneNumber();
        User user = userRepository.findByPhoneNumberAndDelYN(phNumber, DelYN.N).orElseThrow(()->new EntityNotFoundException("없는 회원입니다"));
        user.getFireBaseToken(tokenRequest);

    }

//  2.앱으로부터 알림메세지 보내는 로직(특정 유저의 파이어베이스 토큰을 받아 해당 유저의 앱에 푸시메시지를 보내겠다는 것)
    public void sendHealthSyncReq(String firebaseToken){
        Message message = Message.builder()
                .setToken(firebaseToken) //유저의 firebaseToken을 지정하면 해당 디바이스로 전송됨
                .putData("action","request_health_data") //메시지 안에 커스텀데이터를 넣는 부분. 앱이서 이 걸 보고 어떤 알림인지 인식하기 위한 용도
                .build();
    try {
        String response = FirebaseMessaging.getInstance().send(message); //Firebase SDK를 통해 메세지를 전송. 성공시 메시지 id가 문자열로 반환됨
        System.out.println("FCM메세지 전송 성공" + response);

    } catch (Exception e){
        System.out.println("FCM 전송 실패" + e.getMessage());
    }
    }
//    3. 화상통화 알림 메시지
    public void sendVideoCallNotification(String firebaseToken,String userName){
    Message message = Message.builder()
            .setToken(firebaseToken) //유저의 firebaseToken을 지정하면 해당 디바이스로 전송됨
            .setNotification(Notification.builder()
                    .setTitle("화상통화 요청")
                    .setBody("실버포션 앱을 실행시켜 화상통화를 시작하세요")
                    .build()
            )
            .putData("action","start_video_call") //메시지 안에 커스텀데이터를 넣는 부분. 앱이서 이 걸 보고 어떤 알림인지 인식하기 위한 용도
            .putData("callerId",userName)
            .build();
    try {
        String response = FirebaseMessaging.getInstance().send(message); //Firebase SDK를 통해 메세지를 전송. 성공시 메시지 id가 문자열로 반환됨
        System.out.println("화상통화 알림 전송 성공" + response);

    } catch (Exception e){
        System.out.println("화상통화 알림 전송 실패" + e.getMessage());
    }
}


}
