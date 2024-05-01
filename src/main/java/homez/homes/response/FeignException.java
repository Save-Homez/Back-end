package homez.homes.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class FeignException extends RuntimeException {
    private HttpStatus status;
    private String url;
    private String errorResult;
}
