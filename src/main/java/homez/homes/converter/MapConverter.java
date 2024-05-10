package homez.homes.converter;

import homez.homes.dto.MapResponse;
import homez.homes.dto.MapResponse.Label;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.entity.constant.Mark;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MapConverter {
    public static List<String> toStationNames(List<Station> stations) {
        return stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

    public static Map<String, Integer> toTravelTimeMap(List<TravelTime> travelTimes) {
        return travelTimes.stream()
                .collect(Collectors.toMap(TravelTime::getOrigin, TravelTime::getTime));
    }

    public static MapResponse toMapResponse(List<Station> stations, Map<String, Integer> travelTimeMap) {
        List<Label> labels = stations.stream()
                .map(station -> toLabel(station, travelTimeMap.get(station.getName())))
                .collect(Collectors.toList());
        return new MapResponse(labels);
    }

    private static MapResponse.Label toLabel(Station station, int time) {
        return new MapResponse.Label(station.getCoordinate().getX(), station.getCoordinate().getY(),
                Mark.fromTime(time), station.getAvgDeposit(), station.getAvgRental());
    }
}
