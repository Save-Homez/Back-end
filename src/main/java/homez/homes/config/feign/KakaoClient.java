package homez.homes.config.feign;

import homez.homes.controller.dto.KakaoSubwayResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-client", url = "https://dapi.kakao.com", configuration = KakaoAuthInterceptor.class)
public interface KakaoClient {
    @GetMapping("/v2/local/search/category.JSON?category_group_code=SW8&sort=distance")
    KakaoSubwayResponse getNearbySubways(@RequestParam(value = "x") String longitude,
                                         @RequestParam(value = "y") String latitude,
                                         @RequestParam("radius") Integer radius);
}
