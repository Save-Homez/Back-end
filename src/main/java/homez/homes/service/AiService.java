package homez.homes.service;

import static homez.homes.response.ErrorCode.Ai_NOT_SUPPORTED;
import static homez.homes.response.ErrorCode.CACHE_NOT_FOUND;

import homez.homes.config.feign.AiClient;
import homez.homes.converter.AiConverter;
import homez.homes.dto.AiRequest;
import homez.homes.dto.AiResponse;
import homez.homes.dto.UserInfo;
import homez.homes.entity.AiDto;
import homez.homes.repository.AiDtoRepository;
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
    private final AiDtoRepository aiDtoRepository;

    @CachePut(value = "aiResponses", key = "#username")
    public AiResponse aiAnalyze(String username, UserInfo userInfo) {
        AiDto aiDto = aiDtoRepository.findByStation(userInfo.getDestination())
                .orElseThrow(() -> new CustomException(Ai_NOT_SUPPORTED));
        AiRequest request = AiConverter.toAiRequest(userInfo, aiDto.getTown());

        return aiClient.getAiResult(request);
    }

    @Cacheable(value = "aiResponses", key = "#username")
    public AiResponse getCachedAiResponse(String username) {
        throw new CustomException(CACHE_NOT_FOUND);
    }
}
