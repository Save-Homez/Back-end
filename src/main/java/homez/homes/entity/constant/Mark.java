package homez.homes.entity.constant;

public enum Mark {
    GOOD, SOSO, BAD;

    public static Mark fromTime(int time) {
        if (time < 30) {
            return GOOD;
        }
        if (time < 60) {
            return SOSO;
        }
        return BAD;
    }
}
