package com.silverpotion.chatserver.notification.controller;

import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.service.UserFeign;
import com.silverpotion.chatserver.common.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserFeign userFeign;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@LoginUser String loginUser) {
        System.out.println("subscribe loginUser: " + loginUser);
        if (emitterMap.containsKey(loginUser)) {
            SseEmitter oldEmitter = emitterMap.get(loginUser);
            if (oldEmitter != null) oldEmitter.complete();
            emitterMap.remove(loginUser);
        }

        SseEmitter emitter = new SseEmitter(2 * 60 * 1000L); // 2분 타임아웃
        emitterMap.put(loginUser, emitter);

        try {
            emitter.send(SseEmitter.event().name("connect").data("연결완료"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return emitter;
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
