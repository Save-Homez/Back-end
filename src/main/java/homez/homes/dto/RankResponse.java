package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RankResponse {
    private List<TimeGroup> timeGroups;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TimeGroup {
        private String timeRange;
        private List<TownCard> townCards;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TownCard {
        private String town;
        private int travelTime;
        private int avgDeposit;
        private int avgRental;

        private double x;
        private double y;
    }
}
