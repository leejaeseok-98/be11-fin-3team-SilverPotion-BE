package com.silverpotion.chatserver.chat.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        URI uri = request.getURI();
        String loginId = UriComponentsBuilder.fromUri(uri).build().getQueryParams().getFirst("loginId");

        if (loginId != null && !loginId.isBlank()) {
            attributes.put("loginId", loginId);
            System.out.println("✅ loginId 쿼리 파라미터로 세션 저장됨: " + loginId);
        } else {
            System.out.println("❌ loginId 쿼리 없음");
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
