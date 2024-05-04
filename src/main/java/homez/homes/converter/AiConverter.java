package homez.homes.converter;

import homez.homes.dto.AiRequest;
import homez.homes.dto.UserInfo;
import homez.homes.entity.constant.Sex;
import homez.homes.entity.constant.WorkDay;

public class AiConverter {
    public static AiRequest toAiRequest(UserInfo userInfo, String town) {
        return AiRequest.builder()
                .factors(userInfo.getFactors())
                .town(town)
                .timeRange(userInfo.getTimeRange())
                .sex(Sex.toCode(userInfo.getSex()))
                .age(userInfo.getAge())
                .workDay(WorkDay.toCode(userInfo.getWorkDay()))
                .arrivalTime(userInfo.getArrivalTime())
                .build();
    }
}
