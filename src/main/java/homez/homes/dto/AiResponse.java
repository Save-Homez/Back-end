package homez.homes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiResponse {
    @JsonProperty("pointList")
    private List<AiResult> aiResult;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AiResult {
        @JsonProperty("pointInfo")
        private List<TownResult> townResults;
        private String timeRange;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TownResult {
        private String name;
        private String matchRate;
        private int rank;
    }
}
