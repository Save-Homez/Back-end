package homez.homes.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;

public class KakaoAuthInterceptor implements RequestInterceptor {
    @Value("${kakao.key}")
    private String KAKAO_KEY;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Content-Type", "application/json;charset=UTF-8");
        template.header("Authorization", "KakaoAK " + KAKAO_KEY);
    }
}
