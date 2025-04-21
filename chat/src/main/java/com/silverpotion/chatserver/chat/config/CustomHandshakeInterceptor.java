package com.silverpotion.chatserver.chat.config;

import com.silverpotion.chatserver.chat.service.UserFeign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    private final UserFeign userFeign;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String query = servletRequest.getServletRequest().getQueryString();
            String loginId = servletRequest.getServletRequest().getParameter("loginId");

            if (loginId != null && !loginId.isBlank()) {
                Long userId = userFeign.getUserIdByLoginId(loginId);
                attributes.put("loginId", loginId);
                attributes.put("id", userId);
                log.info("✅ WebSocket Handshake - loginId={}, userId={}", loginId, userId);
            } else {
                log.warn("❌ HandshakeInterceptor: loginId 쿼리 파라미터 없음");
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // 생략 가능
    }
}
