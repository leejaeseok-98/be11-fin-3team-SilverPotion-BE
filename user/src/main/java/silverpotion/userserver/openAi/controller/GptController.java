package silverpotion.userserver.openAi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import silverpotion.userserver.openAi.service.OpenAiService;

@RestController
@RequestMapping("silverpotion/gptchat")
public class GptController {
    private final OpenAiService openAiService;

    public GptController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @GetMapping("/gptqna")
    public Mono<String> chat(@RequestParam String message) {
        return openAiService.chatWithGpt(message);
    }
}
