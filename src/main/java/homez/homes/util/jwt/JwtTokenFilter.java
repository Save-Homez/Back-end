package homez.homes.util.jwt;

import static homez.homes.response.ErrorCode.INVALID_TOKEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

import homez.homes.response.CustomException;
import homez.homes.util.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final String TOKEN_ERROR_MESSAGE = "유효한 인증 토큰이 필요합니다.";
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            processTokenAuthentication(request);
        } catch (CustomException e) {
            log.error("Invalid Token", e);
            ResponseUtils.sendErrorResponse(response, SC_UNAUTHORIZED, TOKEN_ERROR_MESSAGE);
            return;
        }
        filterChain.doFilter(request, response);
    }

    private void processTokenAuthentication(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.hasText(token) && jwtTokenUtils.validateToken(token)) {
            Authentication authentication = jwtTokenUtils.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return;
        }
        log.error("Invalid token for requestURI: {}", request.getRequestURI());
        throw new CustomException(INVALID_TOKEN);
    }

    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
