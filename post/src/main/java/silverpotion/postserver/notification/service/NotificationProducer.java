package silverpotion.postserver.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import silverpotion.postserver.notification.dto.NotificationMessageDto;

@Service
@Slf4j
public class NotificationProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public NotificationProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendNotification(NotificationMessageDto dto) {
        try {
            String message = objectMapper.writeValueAsString(dto);
            kafkaTemplate.send("notification-topic", message);
            log.info("✅ 알림 발행(server: gather): {}", message);
        } catch (JsonProcessingException e) {
            log.error("❌ 알림 발행 실패", e);
        }
    }
}
