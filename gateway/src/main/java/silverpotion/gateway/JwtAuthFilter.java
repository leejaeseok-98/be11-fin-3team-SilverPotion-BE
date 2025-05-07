package silverpotion.gateway;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthFilter implements GlobalFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> ALLOWED_PATHS = List.of(
            "/health",
            "/silverpotion/user/healthcheck",
            "/silverpotion/user/create",
            "/silverpotion/user/checkDuplicate",
            "/silverpotion/user/login",
            "/silverpotion/gathering-category",
            "/silverpotion/gathering-category/detail",
            "/silverpotion/user/refresh-token",
            "/silverpotion/user/google/login",
            "/silverpotion/user/kakao/login",
            "/silverpotion/firebase/token",
            "/silverpotion/health/fromPhone",
            "/connect/**",             // SockJS 엔드포인트 및 하위 경로 허용
            "/chat-service/room/**/read",
            "/chat-service/**/info",                // info 요청 (핸드셰이크용)
            "/chat-service/**/websocket",           // 실제 WebSocket 요청 경로
            "/chat-service/**/xhr*",                // fallback transport 경로들
            "/chat-service/**/eventsource",         // fallback transport
            "/chat-service/**/htmlfile" ,            // 일부 브라우저 fallback
            "/sse/**"

    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getRawPath();
        System.out.println("📍 Request Path = " + path);

        // OPTIONS요청은 인증 없이 바로 통과!
        if (exchange.getRequest().getMethod().name().equals("OPTIONS")) {
            return chain.filter(exchange);
        }

        // ✅ 예외 경로 먼저 처리
        boolean isAllowed = ALLOWED_PATHS.stream().anyMatch(allowed -> pathMatcher.match(allowed, path));
        if (isAllowed) {
            return chain.filter(exchange);
        }

        // ✅ Authorization 헤더 확인
        String bearerToken = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            System.out.println("❌ Authorization 헤더 없음 또는 형식 오류. 차단된 요청: " + path);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            String token = bearerToken.substring(7);

            // ✅ JWT 파싱
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String loginId = claims.getSubject();
            String role = claims.get("role", String.class);
            if (path.contains("/admins/")) {
                if (!"ADMIN".equals(role)) {
                    System.out.println("❌ 관리자 권한이 아닙니다. 차단된 요청: " + path);
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
            }
            Long id = claims.get("id", Long.class);

            // ✅ 커스텀 헤더 추가
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(builder -> builder
                            .header("X-User-LoginId", loginId)
                            .header("X-User-Id", String.valueOf(id))
                            .header("X-User-Role", "ROLE_" + role))
                    .build();

            return chain.filter(modifiedExchange);

        } catch (JwtException e) {
            System.out.println("❌ JWT 파싱 실패: " + e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}