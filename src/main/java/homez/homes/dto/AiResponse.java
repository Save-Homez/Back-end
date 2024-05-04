package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AiResponse {
    private List<AiResult> aiResult;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AiResult {
        private List<TownResult> townResults;
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
