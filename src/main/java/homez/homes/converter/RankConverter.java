package homez.homes.converter;

import homez.homes.dto.RankResponse;
import homez.homes.dto.RankResponse.TimeGroup;
import homez.homes.dto.RankResponse.TownCard;
import homez.homes.entity.constant.TimeRange;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RankConverter {
    public static RankResponse toRankResponse(Map<TimeRange, List<TownCard>> timeMap) {
        List<TimeGroup> timeGroups = timeMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> new TimeGroup(entry.getKey().name(), entry.getValue()))
                .collect(Collectors.toList());
        return new RankResponse(timeGroups);
    }
}
