package homez.homes.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WorkDay {
    WEEKDAY(1), WEEKEND(0);

    private final int code;

    public static int toCode(String name) {
        return WorkDay.valueOf(name).getCode();
    }
}
