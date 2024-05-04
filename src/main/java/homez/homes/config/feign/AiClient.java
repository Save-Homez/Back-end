package homez.homes.config.feign;

import homez.homes.dto.AiRequest;
import homez.homes.dto.AiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-client", url = "${feign.ai-client.url}")
public interface AiClient {
    @PostMapping
    AiResponse getAiResult(@RequestBody AiRequest request);
}
