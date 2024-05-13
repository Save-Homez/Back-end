package homez.homes.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class OpenaiAuthInterceptor implements RequestInterceptor {
    @Value("${openai.key}")
    private String OPENAI_KEY;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", "application/json;charset=UTF-8");
        template.header("Authorization", "Bearer " + OPENAI_KEY);
    }
}
