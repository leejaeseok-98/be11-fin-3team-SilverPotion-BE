package com.silverpotion.chatserver.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.service.UserFeign;
import com.silverpotion.chatserver.common.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
public class SseController {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserFeign userFeign;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/sse/subscribe")
    public SseEmitter subscribe(@LoginUser String loginUser) {
        System.out.println("subscribe loginUser: " + loginUser);
        SseEmitter emitter = new SseEmitter(0L); // 무한 연결
        emitterMap.put(loginUser, emitter);

        emitter.onCompletion(() -> emitterMap.remove(loginUser));
        emitter.onTimeout(() -> emitterMap.remove(loginUser));

        return emitter;
    }
    public void sendToClientOrQueue(String loginId, ChatMessageDto message) {
        SseEmitter emitter = emitterMap.get(loginId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("chat")
                        .data(objectMapper.writeValueAsString(message)));
            } catch (IOException e) {
                emitterMap.remove(loginId);
            }
        } else {
            // DB에 저장 또는 푸시 실패 처리
        }
    }
    @GetMapping("/unsubscribe")
    public void unsubscribe(@LoginUser String loginUser) {
        emitterMap.remove(loginUser);
    }

    public void publishMessage(ChatMessageDto chatMessageDto, Long chatRoomId) {
        List<ChatParticipant> offlineUsers = chatParticipantRepository.findByChatRoomIdAndIsConnectedFalse(chatRoomId);

        for (ChatParticipant participant : offlineUsers) {

            String loginId = userFeign.getLoginIdByUserId(participant.getUserId());
            SseEmitter emitter = emitterMap.get(loginId);

            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("chat-message").data(chatMessageDto));
                } catch (IOException e) {
                    emitterMap.remove(loginId);
                }
            }
        }
    }
}
