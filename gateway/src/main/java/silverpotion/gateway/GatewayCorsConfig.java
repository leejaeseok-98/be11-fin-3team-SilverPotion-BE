package silverpotion.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;

import java.util.List;

@Configuration
public class GatewayCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000",
//                배포시에 배포 주소로 변경
                "https://www.jy1187.shop"
//                "https://d414-220-72-230-176.ngrok-free.app                                                                                                                                                              "
        )); // 정확하게 한 번만 설정
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(); // ✅ reactive 버전 사용
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source); // 타입 일치
    }
}