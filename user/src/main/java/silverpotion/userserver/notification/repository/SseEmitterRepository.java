//package silverpotion.userserver.notification.repository;
//
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//import silverpotion.userserver.notification.dto.NotificationMessage;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class SseEmitterRepository {
//    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();
//
//    public void save(Long userId, SseEmitter emitter) {
//        emitters.put(userId, emitter);
//    }
//
//    public void remove(Long userId) {
//        emitters.remove(userId);
//    }
//
//    public boolean hasUser(Long userId) {
//        return emitters.containsKey(userId);
//    }
//
//    public void sendToUser(Long userId, NotificationMessage msg) {
//        SseEmitter emitter = emitters.get(userId);
//        if (emitter != null) {
//            try {
//                emitter.send(SseEmitter.event().name("notification").data(msg));
//            } catch (IOException e) {
//                remove(userId);
//            }
//        }
//    }
//}
