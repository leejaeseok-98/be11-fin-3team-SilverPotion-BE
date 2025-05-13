package com.silverpotion.chatserver.notification.controller;

import com.silverpotion.chatserver.chat.domain.ChatParticipant;
import com.silverpotion.chatserver.chat.dto.ChatMessageDto;
import com.silverpotion.chatserver.chat.repository.ChatParticipantRepository;
import com.silverpotion.chatserver.chat.service.UserFeign;
import com.silverpotion.chatserver.common.annotation.LoginUser;
import com.silverpotion.chatserver.notification.dto.NotificationMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private final ChatParticipantRepository chatParticipantRepository;
    private final UserFeign userFeign;

    @CrossOrigin(origins = "https://www.silverpotion.site", allowCredentials = "true")
    @GetMapping("/subscribe")
    public SseEmitter subscribe(@RequestParam String loginId) {
        log.info("📡 SSE 구독 요청: {}", loginId);

        // 기존 연결 제거
        if (emitterMap.containsKey(loginId)) {
            SseEmitter oldEmitter = emitterMap.get(loginId);
            if (oldEmitter != null) oldEmitter.complete();
            emitterMap.remove(loginId);
        }

        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L); // 30분
        emitterMap.put(loginId, emitter);

        // 초기 연결 알림
        try {
            emitter.send(SseEmitter.event().name("connect").data("연결완료"));
        } catch (IOException e) {
            log.error("❌ 연결 응답 실패: {}", e.getMessage());
        }

        // ping 스케줄러
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
            }catch (Exception e) {
                log.warn("❌ ping 전송 실패: {} - {}", loginId, e.getMessage());
                emitter.completeWithError(e);
                emitterMap.remove(loginId);
                scheduler.shutdown();
            }
        }, 0, 30, TimeUnit.SECONDS);

        // 종료 처리
        Runnable cleanup = () -> {
            emitterMap.remove(loginId);
            if (!scheduler.isShutdown()) scheduler.shutdown();
        };

        emitter.onTimeout(cleanup);
        emitter.onCompletion(cleanup);
        emitter.onError(e -> {
            log.warn("❌ SSE 오류 발생: {} - {}", loginId, e.getMessage());
            cleanup.run();
        });

        return emitter;
    }

    @GetMapping("/unsubscribe")
    public void unsubscribe(@RequestParam String loginUser) {
        emitterMap.remove(loginUser);
        log.info("🔌 수동 구독 해제: {}", loginUser);
    }

    public void publishMessage(ChatMessageDto chatMessageDto, Long chatRoomId) {
        List<ChatParticipant> offlineUsers = chatParticipantRepository.findByChatRoomIdAndIsConnectedFalse(chatRoomId);

        for (ChatParticipant participant : offlineUsers) {
            String loginId = participant.getLoginId();
            SseEmitter emitter = emitterMap.get(loginId);

            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event().name("chat-message").data(chatMessageDto));
                } catch (IOException e) {
                    log.warn("❌ 채팅 SSE 전송 실패 - {}", e.getMessage());
                    emitter.completeWithError(e);
                    emitterMap.remove(loginId);
                }
            }
        }
    }
//    오버로딩용 sendToClientOrQueue 2개 받는 dto 다름 알림용 채팅용
    public void sendToClientOrQueue(String loginId, ChatMessageDto chatMessage) {
        SseEmitter emitter = emitterMap.get(loginId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("chat-message") // 채팅 메시지 이벤트 이름
                        .data(chatMessage));
                log.info("✅ SSE 채팅 메시지 전송 완료 (loginId: {})", loginId);
            } catch (IOException e) {
                log.warn("❌ SSE 채팅 메시지 전송 실패 (loginId: {}): {}", loginId, e.getMessage());
                emitter.completeWithError(e);
                emitterMap.remove(loginId);
            }
        } else {
            log.info("ℹ️ SSE 미연결 상태 (loginId: {}), 채팅 메시지 보류", loginId);
            // TODO: 메시지 큐 또는 DB에 저장 후 재전송 고려
        }
    }
    public void sendToClientOrQueue(String loginId, NotificationMessageDto message) {
        SseEmitter emitter = emitterMap.get(loginId);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                log.warn("❌ SSE 전송 실패 - 연결 종료됨 (loginId: {}): {}", loginId, e.getMessage());
                emitter.completeWithError(e);
                emitterMap.remove(loginId);
            }
        } else {
            log.info("ℹ️ 로그인되지 않아 SSE 알림 보류 (loginId: {})", loginId);
            // TODO: DB나 큐에 저장하여 이후 재전송 고려
        }
    }
}