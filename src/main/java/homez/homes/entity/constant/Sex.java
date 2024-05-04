package homez.homes.entity.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Sex {
    MALE(1), FEMALE(0);

    private final int code;

    public static int toCode(String name) {
        return Sex.valueOf(name).getCode();
    }
}
