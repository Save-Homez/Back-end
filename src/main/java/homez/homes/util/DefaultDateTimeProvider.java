package homez.homes.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DefaultDateTimeProvider implements DateTimeProvider {
    private static final String ZONE_ID = "Asia/Seoul";

    @Override
    public Date nowDate() {
        return Date.from(ZonedDateTime.now(ZoneId.of(ZONE_ID)).toInstant());
    }

    @Override
    public Date getDateAfterMinutes(int minutes) {
        return Date.from(
                ZonedDateTime.now(ZoneId.of(ZONE_ID))
                        .plus(minutes, ChronoUnit.MINUTES).toInstant());
    }
}
