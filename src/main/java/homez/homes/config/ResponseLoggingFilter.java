package homez.homes.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class ResponseLoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        filterChain.doFilter(request, response);

        // 응답 후처리로 헤더 정보 로깅
        String corsHeader = response.getHeader("Access-Control-Allow-Origin");
        if (corsHeader != null) {
            log.info("Access-Control-Allow-Origin: {}", corsHeader);
        } else {
            log.error("Access-Control-Allow-Origin header is missing");
        }
    }
}
