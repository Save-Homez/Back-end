package homez.homes.controller;

import homez.homes.dto.RankResponse;
import homez.homes.response.Response;
import homez.homes.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rank")
@RequiredArgsConstructor
public class RankController {
    private final RankService rankService;

    @GetMapping
    public Response<RankResponse> getRanks(@RequestParam String destination, Authentication authentication) {
        RankResponse response = rankService.getRanks(destination, authentication.getName());
        return Response.success(response);
    }
}
