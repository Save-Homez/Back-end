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

    /**
     * @return (서울 시간대의 현재 시각 + duration 일) 후의 {@code Date} 반환
     */
    @Override
    public Date getDateAfterDays(int duration) {
        return Date.from(
                ZonedDateTime.now(ZoneId.of(ZONE_ID))
                        .plus(duration, ChronoUnit.DAYS).toInstant());
    }
}
