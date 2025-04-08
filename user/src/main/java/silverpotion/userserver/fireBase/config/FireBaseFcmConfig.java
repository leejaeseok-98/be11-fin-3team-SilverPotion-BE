package silverpotion.userserver.fireBase.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
public class FireBaseFcmConfig {


   @PostConstruct //스프링이 서버시작 시 자동 실행
    public void initialize(){
        try {
            FirebaseOptions options = FirebaseOptions.builder() // Firebase Admin SDK를 초기화하기 위한 옵션을 만드는 빌더
                    .setCredentials( //Firebase에 접속하기 위해 서비스 계정 키(JSON)을 설정
                            //JSON키 파일을 파일 스트림으로 읽어옴. newClassPathResource의 인자에는 리소스 폴더 밑에 있는 파일을 명시
                            GoogleCredentials.fromStream(new ClassPathResource("my-firebase-test-6d7c3-firebase-adminsdk-fbsvc-7093970520.json").getInputStream())
                    ).build();
           //위에서 만든 옵션으로 sdk를 초기화. 이게 있어야 서버에서 FCM에 메세지를 보낼 수 있음
            FirebaseApp.initializeApp(options);
            System.out.println("Fcm 설정 성공");
        } catch (IOException e){
            System.out.println("FCM 연결 오류");
        }

    }







}
