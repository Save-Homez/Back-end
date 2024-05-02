package homez.homes.controller;

import homez.homes.dto.AddressInfo;
import homez.homes.response.Response;
import homez.homes.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping
    public Response<AddressInfo> getAddress(@RequestParam String x, @RequestParam String y) {
        return Response.success(addressService.findStation(x, y));
    }

}
