package com.silverpotion.chatserver.chat.config;

import com.silverpotion.chatserver.chat.service.ChatService;
import io.lettuce.core.ScriptOutputType;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
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

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            System.out.println("✅ WebSocket CONNECT 요청 수신됨");
        }

        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            System.out.println("✅ SUBSCRIBE 요청 수신됨");

            // ✅ 세션에서 loginId 추출
            String userLoginId = (String) accessor.getSessionAttributes().get("loginId");
            String destination = accessor.getDestination();

            System.out.println("userLoginId : "+userLoginId+" destination : "+destination);
            if (userLoginId == null || destination == null) {
                throw new IllegalArgumentException("❌ 세션 정보 또는 destination 없음");
            }

            try {
                Long userId = chatService.getUserIdByLoginId(userLoginId);
                Long roomId = Long.parseLong(destination.split("/")[2]); // /topic/{roomId}
                System.out.println("userId : "+userId+"roomId : "+roomId);
                if (!chatService.isRoomPaticipant(userId, roomId)) {
                    throw new IllegalArgumentException("해당 채팅방에 참여하고 있지 않습니다.");
                }

                System.out.printf("✅ 구독 허용: userId=%d, roomId=%d%n", userId, roomId);
            } catch (Exception e) {
                throw new IllegalArgumentException("❌ 구독 권한 검증 실패", e);
            }
        }

        return message;
    }
}
