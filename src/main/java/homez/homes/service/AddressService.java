package homez.homes.service;

import static homez.homes.response.ErrorCode.NO_NEARBY_SUBWAY_FOUND;

import homez.homes.config.feign.KakaoClient;
import homez.homes.dto.AddressInfo;
import homez.homes.dto.KakaoSubwayResponse;
import homez.homes.dto.KakaoSubwayResponse.Document;
import homez.homes.repository.AiDtoRepository;
import homez.homes.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {
    static private final int RADIUS = 1000;
    private final KakaoClient kakaoClient;
    private final AiDtoRepository aiDtoRepository;

    public AddressInfo findStation(String x, String y) {
        KakaoSubwayResponse response = kakaoClient.getNearbySubways(x, y, RADIUS);
        String station = getSubway(response);
        return new AddressInfo(station);
    }

    private String getSubway(KakaoSubwayResponse response) {
        String station = response.getDocuments().stream()
                .map(Document::getPlaceName)
                .map(placeName -> placeName.split("\\s+"))
                .filter(parts -> parts.length > 1 && parts[1].matches("[1-9]+호선"))
                .findFirst()
                .map(parts -> parts[0])
                .orElseThrow(() -> new CustomException(NO_NEARBY_SUBWAY_FOUND));

        if (!aiDtoRepository.existsStation(station)) {
            throw new CustomException(NO_NEARBY_SUBWAY_FOUND);
        }

        return station;
    }
}
