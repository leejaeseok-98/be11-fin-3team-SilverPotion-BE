package silverpotion.userserver.user.service;

import com.netflix.discovery.provider.Serializer;
import jakarta.ws.rs.core.MultivaluedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import silverpotion.userserver.user.dto.AccessTokenDto;
import silverpotion.userserver.user.dto.GoogleProfileDto;

@Service
public class GoogleService {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-url}")
    private String redirectUrl;

    public AccessTokenDto getAccessToken(String code) {
//  인가코드,client, client-secret, redirect_url,grant_type
        RestClient restClient = RestClient.create();
//        MutiValueMap을 통해 자동으로 form-data형식으로 body 조립 가능
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");
        ResponseEntity<String> response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()//응답 body값만 추출
                .toEntity(String.class);
        System.out.println("응답 json : " + response.getBody());
        return null;
    }

    public GoogleProfileDto getGoogleProfile(String token) {
        return null;
    }
}
