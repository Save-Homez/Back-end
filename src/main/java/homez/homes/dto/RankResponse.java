package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankResponse {
    private List<TimeGroup> timeGroups;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TimeGroup {
        private String timeRange;
        private List<TownCard> townCards;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class TownCard {
        private String town;
        private int travelTime;
        private int avgDeposit;
        private int avgRental;
        private int avgLump;

        private double x;
        private double y;
        private String station;
    }
}
