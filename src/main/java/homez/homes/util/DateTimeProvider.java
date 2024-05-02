package homez.homes.util;

import java.util.Date;

public interface DateTimeProvider {
    Date nowDate();
    Date getDateAfterDays(int duration);
}
