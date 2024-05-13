package homez.homes.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class AiReportRequest {
    @NotNull
    private String town;
    @NotNull
    private String destination;
    @NotNull
    private List<String> factors;
    @NotNull
    private String station;
}
