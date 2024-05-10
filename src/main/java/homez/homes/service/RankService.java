package homez.homes.service;

import static homez.homes.response.ErrorCode.*;

import homez.homes.dto.AiResponse.AiResult;
import homez.homes.dto.AiResponse.TownResult;
import homez.homes.dto.RankResponse;
import homez.homes.dto.RankResponse.TimeGroup;
import homez.homes.dto.RankResponse.TownCard;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.entity.constant.TimeRange;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TravelTimeRepository;
import homez.homes.response.CustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        List<AiResult> results = aiService.getCachedAiResponse(username).getAiResult();
        List<TimeGroup> timeGroups = new ArrayList<>();
        Map<String, Integer> travelTimeMap = getTravelTimeMap(destination);
        for (int i = 0; i < TimeRange.values().length; i++) {
            List<TownResult> towns = results.get(i).getTownResults();
            List<TownCard> townCards = getTownCards(towns, travelTimeMap);
            timeGroups.add(new TimeGroup(TimeRange.getName(i), townCards));
        }
        return new RankResponse(timeGroups);
    }

    private Map<String, Integer> getTravelTimeMap(String destination) {
        List<TravelTime> travelTimes = travelTimeRepository.findByDestination(destination);
        return travelTimes.stream()
                .collect(Collectors.toMap(TravelTime::getOrigin, TravelTime::getTime));
    }

    private List<TownCard> getTownCards(List<TownResult> towns, Map<String, Integer> travelTimeMap) {
        List<TownCard> result = new ArrayList<>();
        for (int i = 0; i < towns.size(); i++) {
            String town = towns.get(i).getName();
            Station station = stationRepository.findByTown(town)
                    .orElseThrow(() -> new CustomException(STATION_NOT_FOUND));
            result.add(new TownCard(town, travelTimeMap.get(station.getName()), station.getAvgDeposit(), station.getAvgRental(),
                    station.getCoordinate().getX(), station.getCoordinate().getY()));
        }
        return result;
    }
}
