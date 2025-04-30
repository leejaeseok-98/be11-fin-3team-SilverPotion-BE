package silverpotion.userserver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import silverpotion.userserver.common.auth.JwtHeaderAuthenticationFilter;
import silverpotion.userserver.user.service.UserService;


import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public PasswordEncoder makePassword(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public JwtHeaderAuthenticationFilter jwtHeaderAuthenticationFilter() {
        return new JwtHeaderAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtHeaderAuthenticationFilter jwtHeaderAuthenticationFilter) throws Exception {
        http
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/silverpotion/user/login", "/silverpotion/user/create","/silverpotion/user/checkDuplicate","/silverpotion/user/refresh-token",
                                "/silverpotion/user/google/login", "/silverpotion/user/kakao/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // ✅ 필터 등록

        return http.build();
    }
}