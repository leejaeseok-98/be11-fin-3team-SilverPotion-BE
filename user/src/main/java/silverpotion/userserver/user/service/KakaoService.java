package silverpotion.userserver.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import silverpotion.userserver.user.dto.AccessTokenDto;
import silverpotion.userserver.user.dto.GoogleProfileDto;
import silverpotion.userserver.user.dto.KakaoProfileDto;

@Service
public class KakaoService {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    public AccessTokenDto getAccessToken(String code) {
//  인가코드,client, client-secret, redirect_url,grant_type
        RestClient restClient = RestClient.create();
//        MutiValueMap을 통해 자동으로 form-data형식으로 body 조립 가능
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()//응답 body값만 추출
                .toEntity(AccessTokenDto.class);
        System.out.println("응답 json : " + response.getBody());
        return response.getBody();
    }

    public KakaoProfileDto getKakaoProfile(String token) {
        RestClient restClient = RestClient.create();
        ResponseEntity<KakaoProfileDto> response = restClient.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer "+token)
                .retrieve()//응답 body값만 추출
                .toEntity(KakaoProfileDto.class);
        System.out.println("profile Json : " + response.getBody());
        return response.getBody();
    }
}
