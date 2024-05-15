package homez.homes.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeRange {
    WITHIN_30_MINUTES(0),
    WITHIN_60_MINUTES(1),
    WITHIN_90_MINUTES(2),
    OVER_90_MINUTES(3);

    private final int code;

    public static String getName(int code) {
        for (TimeRange value : values()) {
            if (value.code == code) {
                return value.toString();
            }
        }
        return null;
    }

    public static TimeRange fromTime(int time) {
        if (time <= 20) {
            return WITHIN_30_MINUTES;
        }
        if (time <= 40) {
            return WITHIN_60_MINUTES;
        }
        if (time <= 60) {
            return WITHIN_90_MINUTES;
        }
        return OVER_90_MINUTES;
    }
}
