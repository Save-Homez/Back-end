package homez.homes.service;

import static homez.homes.response.ErrorCode.CACHE_NOT_FOUND;
import static homez.homes.response.ErrorCode.STATION_NOT_FOUND;

import homez.homes.config.feign.AiClient;
import homez.homes.converter.AiConverter;
import homez.homes.dto.AiRequest;
import homez.homes.dto.AiResponse;
import homez.homes.dto.UserInfo;
import homez.homes.repository.StationRepository;
import homez.homes.repository.StationTownOnly;
import homez.homes.response.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AiService {
    private final AiClient aiClient;
    private final StationRepository stationRepository;

    @CachePut(value = "aiResponses", key = "#username")
    public AiResponse aiAnalyze(String username, UserInfo userInfo) {
        StationTownOnly station = stationRepository.findByName(userInfo.getStation())
                .orElseThrow(() -> new CustomException(STATION_NOT_FOUND));
        AiRequest request = AiConverter.toAiRequest(userInfo, station.getTown());

        return aiClient.getAiResult(request);
    }

    @Cacheable(value = "aiResponses", key = "#username")
    public AiResponse getCachedAiResponse(String username) {
        throw new CustomException(CACHE_NOT_FOUND);
    }
}
