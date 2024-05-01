package homez.homes.response;

import static org.springframework.http.HttpStatus.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Address 에러
    NO_NEARBY_SUBWAY_FOUND(NOT_FOUND, "근처 1km 내에 서울 1~9호선 지하철역이 없습니다."),

    // Token 에러
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
