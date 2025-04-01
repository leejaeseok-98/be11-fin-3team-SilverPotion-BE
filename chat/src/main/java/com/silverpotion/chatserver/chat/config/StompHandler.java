package com.silverpotion.chatserver.chat.config;

import com.silverpotion.chatserver.chat.service.ChatService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class StompHandler implements ChannelInterceptor {

    private final ChatService chatService;

    public StompHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("WebSocket CONNECT 요청 수신됨 - 이미 인증된 사용자로 간주");
        }

        if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            String userId = accessor.getFirstNativeHeader("X-User-id");
            String roomId = accessor.getDestination().split("/")[2];

            if (!chatService.isRoomPaticipant(userId, Long.parseLong(roomId))) {
                throw new AuthenticationServiceException("해당 room에 권한이 없습니다.");
            }
        }

        return message;
    }
}
