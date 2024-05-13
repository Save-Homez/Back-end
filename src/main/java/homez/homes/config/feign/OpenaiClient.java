package homez.homes.config.feign;

import homez.homes.dto.OpenaiRequest;
import homez.homes.dto.OpenaiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "openai-client", url = "https://api.openai.com/v1", configuration = OpenaiAuthInterceptor.class)
public interface OpenaiClient {
    @PostMapping("/chat/completions")
    OpenaiResponse generateTotalStatement(@RequestBody OpenaiRequest request);
}
