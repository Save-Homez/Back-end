package homez.homes.dto;

import homez.homes.entity.constant.TimeRange;
import homez.homes.util.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class AiReportRequest {
    @ValidEnum(enumClass = TimeRange.class)
    private String timeRange;
    @NotNull
    private String town;
    @NotNull
    private String destination;
    @NotNull
    private List<String> factors;
}
