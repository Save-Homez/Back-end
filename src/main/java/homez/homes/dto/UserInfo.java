package homez.homes.dto;

import homez.homes.entity.constant.Sex;
import homez.homes.entity.constant.TimeRange;
import homez.homes.entity.constant.WorkDay;
import homez.homes.util.ValidEnum;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;

@Getter
public class UserInfo {
    @NotNull
    @ValidEnum(enumClass = Sex.class)
    private String sex;

    @NotNull
    private int age;

    @NotNull
    @ValidEnum(enumClass = WorkDay.class)
    private String workDay;

    @NotNull
    @Min(value = 0)
    @Max(value = 23)
    private int arrivalTime;

    @NotNull
    private List<String> factors;

    @NotNull
    private String station;

    @NotNull
    @ValidEnum(enumClass = TimeRange.class)
    private String timeRange;
}
