package com.silverpotion.chatserver.videoChat.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
//웹소켓 서버를 설정하고 실행할 수 있게 함. WebSocketConfigurer구현체에서 핸들러 등록을 허용하게 됨
@EnableWebSocket
public class WebSocketConfig  implements WebSocketConfigurer {

    private final SignalingHandler signalingHandler;

    public WebSocketConfig(SignalingHandler signalingHandler) {
        this.signalingHandler = signalingHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(signalingHandler,"/signal").setAllowedOrigins("*");
    }
}
