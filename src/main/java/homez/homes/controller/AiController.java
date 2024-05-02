package homez.homes.controller;

import homez.homes.dto.UserInfo;
import homez.homes.response.Response;
import homez.homes.service.AiService;
import homez.homes.util.jwt.JwtTokenUtils;
import homez.homes.util.jwt.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final JwtTokenUtils jwtTokenUtils;
    private final AiService aiService;

    @PostMapping("/on-board")
    public Response<TokenInfo> progressOnboard(@RequestBody @Valid UserInfo userInfo) {
        TokenInfo token = jwtTokenUtils.generateToken();
        String username = jwtTokenUtils.getUsernameFromToken(token.getAccessToken());

        return Response.success(token);
    }
}
