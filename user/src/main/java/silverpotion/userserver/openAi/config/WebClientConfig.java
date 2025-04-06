package silverpotion.userserver.openAi.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {


    @Value("${openai.api-key}")
    private String apiKey;
    //챗지피티에게 보낼 webclient를 통한 http요청 설정
    @Bean
    @Qualifier("openAi")
    public WebClient openAiWebClient(){
        return WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }


}
