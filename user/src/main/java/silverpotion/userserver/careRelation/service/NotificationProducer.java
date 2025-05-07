package silverpotion.userserver.careRelation.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import silverpotion.userserver.careRelation.dtos.NotificationMessageDto;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNotification(NotificationMessageDto messageDto) {
        try {
            String message = objectMapper.writeValueAsString(messageDto);
            kafkaTemplate.send("notification-topic", message);
            log.info("📤 보호관계 알림 전송됨 → {}", messageDto.getLoginId());
        } catch (JsonProcessingException e) {
            log.error("❌ 알림 직렬화 실패", e);
        }
    }
}