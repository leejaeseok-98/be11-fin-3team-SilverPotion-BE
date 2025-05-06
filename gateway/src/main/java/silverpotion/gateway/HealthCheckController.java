package silverpotion.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/gateway")
public class HealthCheckController {
    @GetMapping("/health")
    public String health(){
        return "ok";

    }

}
