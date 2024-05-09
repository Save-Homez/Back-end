package homez.homes.service;

import homez.homes.converter.MapConverter;
import homez.homes.dto.MapResponse;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TravelTimeRepository;
import java.util.List;
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

    public MapResponse getLabels(String origin, double longitude, double latitude, int radius) {
        List<Station> stations = stationRepository.findWithinRadius(longitude, latitude, radius)
                .stream()
                .filter(s -> !s.getName().equals(origin))
                .collect(Collectors.toList());
        List<TravelTime> travelTimes = travelTimeRepository
                .findByOriginAndDestinations(origin, MapConverter.toStationNames(stations));
        return MapConverter.toMapResponse(stations, MapConverter.toTravelTimeMap(travelTimes));
    }

}
