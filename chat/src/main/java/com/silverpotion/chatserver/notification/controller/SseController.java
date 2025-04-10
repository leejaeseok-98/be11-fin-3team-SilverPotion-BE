//package com.silverpotion.chatserver.notification.controller;
//
//import com.silverpotion.chatserver.notification.repository.SseEmitterRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//@RestController
//@RequiredArgsConstructor
//public class SseController {
//    private final SseEmitterRepository emitterRepository;
//
//    @GetMapping("/sse/subscribe")
//    public SseEmitter subscribe(@RequestParam Long userId) {
//        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
//        emitterRepository.save(userId, emitter);
//
//        emitter.onCompletion(() -> emitterRepository.remove(userId));
//        emitter.onTimeout(() -> emitterRepository.remove(userId));
//
//        return emitter;
//    }
//}
