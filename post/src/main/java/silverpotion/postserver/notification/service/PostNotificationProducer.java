package silverpotion.postserver.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import silverpotion.postserver.notification.dto.NotificationMessage;

@Service
@RequiredArgsConstructor
public class PostNotificationProducer {
    private final KafkaTemplate<String, NotificationMessage> kafkaTemplate;

    public void send(NotificationMessage message) {
        kafkaTemplate.send("notification-events", message);
    }
}
