package homez.homes.util;

import java.time.LocalDate;
import java.util.Date;

public interface DateTimeProvider {
    LocalDate nowDate();
    Date getDateAfterDays(int duration);
}
