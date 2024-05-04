package homez.homes.controller;

import homez.homes.dto.AgencyResponse;
import homez.homes.dto.PropertyResponse;
import homez.homes.response.Response;
import homez.homes.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/ai")
    public Response<Void> getAiReport() {
        return Response.success();
    }

    @GetMapping("/property")
    public Response<PropertyResponse> getProperties(@RequestParam String town) {
        return Response.success(reportService.getProperties(town));
    }

    @GetMapping("/agency")
    public Response<AgencyResponse> getAgencies(@RequestParam String town) {
        return Response.success(reportService.getAgencies(town));
    }
}
