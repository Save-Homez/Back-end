package homez.homes.entity.constant;

public enum Mark {
    GOOD, SOSO, BAD;

    public static Mark fromTime(int time) {
        if (time <= 20) {
            return GOOD;
        }
        if (time <= 40) {
            return SOSO;
        }
        return BAD;
    }
}
