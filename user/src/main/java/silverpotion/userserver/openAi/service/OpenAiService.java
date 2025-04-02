package silverpotion.userserver.openAi.service;

import org.springframework.beans.factory.annotation.Value;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Transactional
public class OpenAiService {
    private final WebClient webClient;

    public OpenAiService(@Value("${openai.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    public Mono<String> chatWithGpt(String userMessage) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o",
                "messages", new Object[]{
                        Map.of("role", "system", "content", "You are a helpful assistant."),
                        Map.of("role", "user", "content", userMessage)
                },
                "temperature", 0.7
        );

        return webClient.post()
                .uri("/chat/completions")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    var choices = (java.util.List<Map<String, Object>>) response.get("choices");
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                });
    }
}
