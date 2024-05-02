package homez.homes.dto;

import homez.homes.entity.constant.TimeRange;
import homez.homes.util.ValidEnum;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class UserInfo {
    @NotNull
    private List<String> factors;

    @NotNull
    private String station;

    @NotNull
    @ValidEnum(enumClass = TimeRange.class)
    private String timeRange;
}
