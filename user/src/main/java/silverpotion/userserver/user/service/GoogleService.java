package silverpotion.userserver.user.service;

import com.netflix.discovery.provider.Serializer;
import jakarta.ws.rs.core.MultivaluedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import silverpotion.userserver.user.dto.AccessTokenDto;
import silverpotion.userserver.user.dto.GoogleProfileDto;

@Service
public class GoogleService {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    public AccessTokenDto getAccessToken(String code) {
//  인가코드,client, client-secret, redirect_url,grant_type
        RestClient restClient = RestClient.create();
//        MutiValueMap을 통해 자동으로 form-data형식으로 body 조립 가능
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        ResponseEntity<AccessTokenDto> response = restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(params)
                .retrieve()//응답 body값만 추출
                .toEntity(AccessTokenDto.class);
        System.out.println("응답 json : " + response.getBody());
        return response.getBody();
    }

    public GoogleProfileDto getGoogleProfile(String token) {
        RestClient restClient = RestClient.create();
        ResponseEntity<GoogleProfileDto> response = restClient.get()
                .uri("https://openidconnect.googleapis.com/v1/userinfo")
                .header("Authorization", "Bearer "+token)
                .retrieve()//응답 body값만 추출
                .toEntity(GoogleProfileDto.class);
        System.out.println("profile Json : " + response.getBody());
        return response.getBody();
    }
}
