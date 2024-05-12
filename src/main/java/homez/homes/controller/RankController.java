package homez.homes.controller;

import homez.homes.dto.RankResponse;
import homez.homes.dto.RankResponse.TimeGroup;
import homez.homes.dto.RankResponse.TownCard;
import homez.homes.response.Response;
import homez.homes.service.RankService;
import java.util.ArrayList;
import java.util.Arrays;
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
//        RankResponse response = rankService.getRanks(destination, authentication.getName());
//        return Response.success(response);
        return Response.success(createRankResponse());
    }

    public RankResponse createRankResponse() {
        return RankResponse.builder()
                .timeGroups(new ArrayList<>(Arrays.asList(
                        TimeGroup.builder()
                                .timeRange("WITHIN_30_MINUTES")
                                .townCards(new ArrayList<>(Arrays.asList(
                                        TownCard.builder().town("신촌동").travelTime(20).avgDeposit(1000).avgRental(500).x(126.94783366705356).y(37.5622375470803).build(),
                                        TownCard.builder().town("사직동").travelTime(25).avgDeposit(1100).avgRental(550).x(126.94783366705356).y(37.5622375470803).build()
                                ))).build(),
                        TimeGroup.builder()
                                .timeRange("WITHIN_60_MINUTES")
                                .townCards(new ArrayList<>(Arrays.asList(
                                        TownCard.builder().town("합정동").travelTime(30).avgDeposit(800).avgRental(400).x(126.94783366705356).y(37.5622375470803).build(),
                                        TownCard.builder().town("문정1동").travelTime(10).avgDeposit(780).avgRental(390).x(126.94783366705356).y(37.5622375470803).build()
                                ))).build(),
                        TimeGroup.builder()
                                .timeRange("WITHIN_90_MINUTES")
                                .townCards(new ArrayList<>(Arrays.asList(
                                                TownCard.builder().town("대흥동").travelTime(10).avgDeposit(780).avgRental(390).x(126.94783366705356).y(37.5622375470803).build())))
                                .build(),
                        TimeGroup.builder()
                                .timeRange("OVER_90_MINUTES")
                                .townCards(new ArrayList<>(Arrays.asList(
                                        TownCard.builder().town("북가좌1동").travelTime(12).avgDeposit(770).avgRental(385).x(126.94783366705356).y(37.5622375470803).build()
                                ))).build()
                )))
                .build();
    }
}
