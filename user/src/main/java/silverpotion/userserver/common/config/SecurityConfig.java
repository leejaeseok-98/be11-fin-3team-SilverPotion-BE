package silverpotion.userserver.common.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import silverpotion.userserver.common.auth.JwtHeaderAuthenticationFilter;
import silverpotion.userserver.user.service.UserService;


import java.util.Arrays;
import java.util.function.Supplier;

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
                        .requestMatchers("/silverpotion/user/healthcheck","/silverpotion/user/login", "/silverpotion/user/create","/silverpotion/user/checkDuplicate"
                                ,"/silverpotion/user/refresh-token", "/silverpotion/user/google/login", "/silverpotion/user/kakao/login" , "/silverpotion/firebase/token","/silverpotion/health/fromPhone").permitAll()
                        .requestMatchers("/silverpotion/user/**","/silverpotion/gatheringvector/**","silverpotion/gathering/**").access(this::internalOrAuthenticated)
                        .requestMatchers("/silverpotion/admins/**").hasAnyRole("ADMIN","SUPER_ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtHeaderAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 필터 등록

        return http.build();
    }

    private AuthorizationDecision internalOrAuthenticated(Supplier<Authentication> authentication, RequestAuthorizationContext context) {
        HttpServletRequest request = context.getRequest();
        String internal = request.getHeader("Internal-Request");

        //Internal-Request: true이면 인증 없이 허용
        if("true".equalsIgnoreCase(internal)){
            return new AuthorizationDecision(true);
        }

        //그 외는 jwt인증이 완료된 경우만 통과
        Authentication auth = authentication.get();
        return new AuthorizationDecision(auth != null && auth.isAuthenticated());
    }
}