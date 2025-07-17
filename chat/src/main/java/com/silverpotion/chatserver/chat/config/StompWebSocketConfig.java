package com.silverpotion.chatserver.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    private final CustomHandshakeInterceptor interceptor;

    public StompWebSocketConfig(StompHandler stompHandler, CustomHandshakeInterceptor interceptor) {
        this.stompHandler = stompHandler;
        this.interceptor = interceptor;
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect")
                .setAllowedOrigins("https://www.silverpotion.site","localhost:3000")
                .addInterceptors(interceptor)
                .setHandshakeHandler(new CustomHandshakeHandler())
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        /publish/1형태로 메시지 발행해야 함을 설정
//        /publish로 시작하는 url패턴으로 메시지가 발행되면 @Controller 객체의 @MessaMapping메서드로 라우팅
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub","/user","topic");

        registry.setUserDestinationPrefix("/user");

    }


//    웹소켓요청(connect, subscribe, disconnect)등의 요청시에는 http header등 http메시지를 넣어올수 있고, 이를 interceptor를 통해 가로채 토큰등을 검증할수 있음.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
