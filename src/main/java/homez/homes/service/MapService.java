package homez.homes.service;

import homez.homes.converter.MapConverter;
import homez.homes.dto.MapResponse;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TravelTimeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MapService {
    private final StationRepository stationRepository;
    private final TravelTimeRepository travelTimeRepository;

    public MapResponse getLabels(String destination, double longitude, double latitude, int radius) {
        List<Station> stations = stationRepository.findWithinRadius(longitude, latitude, radius)
                .stream()
                .filter(s -> !s.getName().equals(destination))
                .collect(Collectors.toList());
        stations = stations.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                Station::getName,
                                Function.identity(),
                                (existing, replacement) -> existing),
                        map -> new ArrayList<>(map.values())
                ));
        List<TravelTime> travelTimes = travelTimeRepository
                .findByOriginAndDestinations(destination, MapConverter.toStationNames(stations));
        return MapConverter.toMapResponse(stations, MapConverter.toTravelTimeMap(travelTimes));
    }

}
