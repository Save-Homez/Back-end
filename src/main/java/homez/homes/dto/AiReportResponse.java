package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiReportResponse {
    private String totalStatement;
    private List<Factor> graph;

    private double matchRate;

    private int travelTime;
    private String station;
    private int avgDeposit;
    private int avgRental;
    private int avgLump;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Factor {
        private String name;
        private String percent;
    }
}
