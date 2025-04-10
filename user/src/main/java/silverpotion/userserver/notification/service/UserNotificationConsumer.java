//package silverpotion.userserver.notification.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import silverpotion.userserver.notification.dto.NotificationMessage;
//import silverpotion.userserver.notification.repository.SseEmitterRepository;
//
//@Service
//@RequiredArgsConstructor
//public class UserNotificationConsumer {
//    private final SseEmitterRepository emitterRepository;
//
//    @KafkaListener(topics = "notification-events", groupId = "gathering-service")
//    public void consume(NotificationMessage msg) {
//        if (!emitterRepository.hasUser(msg.getReceiverId())) return;
//
//        emitterRepository.sendToUser(msg.getReceiverId(), msg);
//    }
//}
