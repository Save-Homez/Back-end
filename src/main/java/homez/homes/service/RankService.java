package homez.homes.service;

import static homez.homes.entity.constant.TimeRange.OVER_90_MINUTES;
import static homez.homes.entity.constant.TimeRange.WITHIN_30_MINUTES;
import static homez.homes.entity.constant.TimeRange.WITHIN_60_MINUTES;
import static homez.homes.entity.constant.TimeRange.WITHIN_90_MINUTES;
import static homez.homes.entity.constant.TimeRange.fromTime;
import static homez.homes.response.ErrorCode.DATABASE_ERROR;
import static homez.homes.response.ErrorCode.STATION_NOT_FOUND;

import homez.homes.converter.RankConverter;
import homez.homes.dto.AiResponse.TownResult;
import homez.homes.dto.RankResponse;
import homez.homes.dto.RankResponse.TownCard;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.entity.constant.TimeRange;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TravelTimeRepository;
import homez.homes.response.CustomException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RankService {
    private final AiService aiService;
    private final StationRepository stationRepository;
    private final TravelTimeRepository travelTimeRepository;

    public RankResponse getRanks(String destination, String username) {
        List<TownResult> aiResult = aiService.getCachedAiResponse(username).getAiResult();

        travelTimeRepository.findByDestination(destination);
        stationRepository.findAll();

        Map<TimeRange, List<TownCard>> timeMap = getTimeMap(aiResult, destination);

        return RankConverter.toRankResponse(timeMap);
    }

    private Map<TimeRange, List<TownCard>> getTimeMap(List<TownResult> aiResult, String destination) {
        Map<TimeRange, List<TownCard>> timeMap = initTimeMap();
        Map<TimeRange, Integer> countMap = initCountMap();

        for (TownResult townResult : aiResult) {
            TownCard townCard = getTownCard(destination, townResult.getTown());
            TimeRange timeRange = fromTime(townCard.getTravelTime());

            if (countMap.getOrDefault(timeRange, 0) < 10) {
                timeMap.computeIfAbsent(timeRange, k -> new ArrayList<>()).add(townCard);
                countMap.put(timeRange, countMap.getOrDefault(timeRange, 0) + 1);
            }

            if (countMap.values().stream().allMatch(count -> count >= 10)) {
                break;
            }
        }
        return timeMap;
    }

    private TownCard getTownCard(String destination, String town) {
        List<Station> stations = stationRepository.findByTown(town)
                .orElseThrow(() -> new CustomException(STATION_NOT_FOUND, "해당 동네의 역을 찾을 수 없습니다."));

        int minTime = Integer.MAX_VALUE;
        Station station = null;
        TravelTime travelTime = null;
        for (Station curStation : stations) {
            TravelTime candidate = travelTimeRepository.findByOriginAndDestination(curStation.getName(), destination)
                    .orElseThrow(() -> new CustomException(DATABASE_ERROR, "해당 동네의 통근 시간을 알 수 없습니다."));

            if (candidate.getTime() < minTime) {
                minTime = candidate.getTime();
                travelTime = candidate;
                station = curStation;
            }
        }

        return new TownCard(town, travelTime.getTime(), station.getAvgDeposit(), station.getAvgRental(), station.getAvgLump(),
                station.getCoordinate().getX(), station.getCoordinate().getY(), station.getName());
    }

    private Map<TimeRange, List<TownCard>> initTimeMap() {
        Map<TimeRange, List<TownCard>> timeMap = new HashMap<>();
        timeMap.put(WITHIN_30_MINUTES, new ArrayList<>());
        timeMap.put(WITHIN_60_MINUTES, new ArrayList<>());
        timeMap.put(WITHIN_90_MINUTES, new ArrayList<>());
        timeMap.put(OVER_90_MINUTES, new ArrayList<>());
        return timeMap;
    }

    private Map<TimeRange, Integer> initCountMap() {
        Map<TimeRange, Integer> countMap = new HashMap<>();
        countMap.put(WITHIN_30_MINUTES, 0);
        countMap.put(WITHIN_60_MINUTES, 0);
        countMap.put(WITHIN_90_MINUTES, 0);
        countMap.put(OVER_90_MINUTES, 0);
        return countMap;
    }
}
