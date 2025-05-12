package com.silverpotion.chatserver.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class KafkaConfig {
    @Bean
    public String kafkaGroupId() {
        return "chat-consumer-" + UUID.randomUUID();
    }
}
