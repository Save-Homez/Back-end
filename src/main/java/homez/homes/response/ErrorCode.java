package homez.homes.response;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common 에러
    _NOT_FOUND(NOT_FOUND, "해당 값이 없습니다."),
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "DB 에러 입니다."),

    // Address 에러
    NO_NEARBY_SUBWAY_FOUND(NOT_FOUND, "근처 1km 내에 서울 1~9호선 지하철역이 없습니다."),

    // Token 에러
    INVALID_TOKEN(UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    // Cache 에러
    CACHE_NOT_FOUND(NOT_FOUND, "해당 캐시가 존재하지 않습니다."),

    // Station 에러
    STATION_NOT_FOUND(NOT_FOUND, "DB에 해당 역이 존재하지 않습니다."),

    // Town 에러
    TOWN_NOT_FOUND(NOT_FOUND, "DB에 해당 동네가 존재하지 않습니다."),

    // Cache 에러
    CACHE_DATA_NOT_FOUND(NOT_FOUND, "해당 캐시 데이터가 없습니다."),

    // Ai 에러
    Ai_NOT_SUPPORTED(NOT_IMPLEMENTED, "지원하지 않는 데이터 입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
