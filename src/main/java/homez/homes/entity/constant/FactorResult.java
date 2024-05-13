package homez.homes.entity.constant;

import static homez.homes.response.ErrorCode.FACTOR_NOT_FOUND;

import homez.homes.response.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FactorResult {
    PHARMACY("약국"),
    WOMEN_WELFARE("여성 복지 시설"),
    EDUCATION("교육 시설"),
    CULTURE("문화 복지 시설"),
    MOVIE("영화관"),
    ART("미술관"),
    PARK("공원"),
    LIBRARY("도서관"),
    GREEN("녹지 분포"),
    NOISE("소음"),
    AIR("대기청결도"),
    REST("휴식공간"),
    WATER("수질오염"),
    SAFETY("치안"),
    CLEAN("깨끗한 길거리"),
    PARKING("주차질서");

    private final String name;

    public static FactorResult fromName(String name) {
        for (FactorResult factor : FactorResult.values()) {
            if (factor.getName().equals(name)) {
                return factor;
            }
        }
        throw new CustomException(FACTOR_NOT_FOUND);
    }
}
