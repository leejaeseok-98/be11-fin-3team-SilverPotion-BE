package com.silverpotion.chatserver.chat.config;

import com.silverpotion.chatserver.chat.service.ChatRoomService;
import com.silverpotion.chatserver.chat.service.UserFeign;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatService;
    private final UserFeign userFeign;

    public StompHandler(ChatRoomService chatService, UserFeign userFeign) {
        this.chatService = chatService;
        this.userFeign = userFeign;
    }

    static class StompPrincipal implements Principal {
        private final String name;
        public StompPrincipal(String name) {
            this.name = name;
        }
        @Override
        public String getName() {
            return name;
        }
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String loginId = accessor.getFirstNativeHeader("X-User-LoginId");
            Long id = userFeign.getUserIdByLoginId(loginId);

            if (loginId != null && id != null) {
                // ✅ 세션에 저장
                accessor.getSessionAttributes().put("loginId", loginId);
                accessor.getSessionAttributes().put("id", id);

                // ✅ WebSocket 메시지 브로커용 Principal 설정
                accessor.setUser(new StompPrincipal(loginId));

                System.out.println("🧩 STOMP CONNECT: sessionId = " + accessor.getSessionId());
                System.out.println("🧩 STOMP CONNECT: Principal = " + accessor.getUser());
                System.out.println("✅ STOMP CONNECT: Principal 설정됨 - loginId=" + loginId);
            } else {
                System.out.println("❌ STOMP CONNECT: 헤더 누락 또는 유효하지 않음");
            }
        }

        return message;
    }
}
