package silverpotion.userserver.common.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class JwtHeaderAuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String loginId = request.getHeader("X-User-LoginId"); // 로그인 아이디
        String userId = request.getHeader("X-User-Id");       // 유저 ID
        String role = request.getHeader("X-User-Role");       // ex) ROLE_ADMIN

        if (loginId != null && role != null) {
            // 1.권한 생성
            Collection<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
            System.out.println("인증된 권한 :" + authorities);
            // 2.인증 객체 생성 (로그인아이디, credentials 없음, 권한 목록)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(loginId, null, authorities);

            // 3.SecurityContext에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
