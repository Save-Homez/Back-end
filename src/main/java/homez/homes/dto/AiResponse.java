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
    private List<TownResult> aiResult;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TownResult {
        @JsonProperty("name")
        private String town;

        private String matchRate;
    }
}
