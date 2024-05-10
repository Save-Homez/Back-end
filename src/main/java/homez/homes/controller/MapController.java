package homez.homes.controller;

import homez.homes.dto.MapResponse;
import homez.homes.response.Response;
import homez.homes.service.MapService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/map")
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/{destination}")
    public Response<MapResponse> getLabels(@PathVariable String destination, @RequestParam(value = "x") double longitude,
                                           @RequestParam(value = "y") double latitude, @RequestParam int radius) {
        MapResponse response = mapService.getLabels(destination, longitude, latitude, radius);
        return Response.success(response);
    }
}
