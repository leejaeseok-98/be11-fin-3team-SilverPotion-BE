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

@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatRoomService chatService;
    private final UserFeign userFeign;

    public StompHandler(ChatRoomService chatService, UserFeign userFeign) {
        this.chatService = chatService;
        this.userFeign = userFeign;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String loginId = accessor.getFirstNativeHeader("X-User-LoginId");
            Long id = userFeign.getUserIdByLoginId(loginId);
            if (loginId != null && id != null) {

                accessor.getSessionAttributes().put("loginId", loginId);
                accessor.getSessionAttributes().put("id", id);
                System.out.println("✅ STOMP CONNECT: 세션 저장됨 - loginId=" + loginId + ", id=" + id);
            } else {
                System.out.println("❌ STOMP CONNECT: 헤더 누락 - loginId=" + loginId + ", userId=" + id);
            }
        }

        return message;
    }
}
