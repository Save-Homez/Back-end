package homez.homes.config;

import homez.homes.util.DateTimeProvider;
import homez.homes.util.DefaultDateTimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public DateTimeProvider dateTimeProvider() {
        return new DefaultDateTimeProvider();
    }
}
