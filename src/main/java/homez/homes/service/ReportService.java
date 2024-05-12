package homez.homes.service;

import static homez.homes.response.ErrorCode._NOT_FOUND;

import homez.homes.converter.ReportConverter;
import homez.homes.dto.AgencyResponse;
import homez.homes.dto.AiReportRequest;
import homez.homes.dto.AiReportResponse;
import homez.homes.dto.AiReportResponse.Factor;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Agency;
import homez.homes.entity.Property;
import homez.homes.entity.Station;
import homez.homes.entity.TravelTime;
import homez.homes.repository.AgencyRepository;
import homez.homes.repository.PropertyRepository;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TownGraphRepository;
import homez.homes.repository.TravelTimeRepository;
import homez.homes.response.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final int PAGE_SIZE = 10;

    private final PropertyRepository propertyRepository;
    private final AgencyRepository agencyRepository;
    private final TownGraphRepository townGraphRepository;
    private final StationRepository stationRepository;
    private final TravelTimeRepository travelTimeRepository;
    private final AiService aiService;

    public AiReportResponse getAiReport(AiReportRequest request, String username) {
//        TownResult result = getTownResult(request.getTimeRange(), request.getTown(), username);

        String totalStatement = generateTotalStatement();
        List<Factor> graph = generateGraph(request.getTown(), request.getFactors());
//        String matchRate = result.getMatchRate();
        double matchRate = 88.88;

//        Station station = stationRepository.findByTown(request.getTown())
//                .orElseThrow(() -> new CustomException(STATION_NOT_FOUND));
        Station station = new Station(100L, "신촌동역", "신촌동", null,
                3000, 50, 5000);
        TravelTime travelTime = travelTimeRepository.findByOriginAndDestination(station.getName(),
                        request.getDestination())
                .orElseThrow(() -> new CustomException(_NOT_FOUND, "해당 TravelTime이 없습니다."));

        return new AiReportResponse(totalStatement, graph, matchRate, travelTime.getTime(), station.getName(),
                station.getAvgDeposit(), station.getAvgRental(), station.getAvgLump());
    }

    private List<Factor> generateGraph(String town, List<String> onBoard) {
//        TownGraph townGraph = townGraphRepository.findByTown(town)
//                .orElseThrow(() -> new CustomException(TOWN_NOT_FOUND));

        Factor f1 = new Factor("영화관", 33);
        Factor f2 = new Factor("미술관", 89);
        Factor f3 = new Factor("문화 복지 시설", 100);
        Factor f4 = new Factor("여성 복지 시설", 0);
        Factor f5 = new Factor("약국", 20);
        Factor f6 = new Factor("녹지 분포", 78);

        return List.of(f1, f2, f3, f4, f5, f6);
    }

    private String generateTotalStatement() {
        return "종합 분석 결과 어쩌고 저쩌고 블라블라";
    }
//
//    private TownResult getTownResult(String timeRange, String town, String username) {
//        List<TownResult> results = aiService.getCachedAiResponse(username).getAiResult()
//                .get(TimeRange.valueOf(timeRange).getCode()).getTownResults();
//        return results.stream()
//                .filter(r -> r.getName().equals(town))
//                .findFirst()
//                .orElseThrow(() -> new CustomException(CACHE_NOT_FOUND, "캐시 값에 해당 동네가 존재하지 않습니다."));
//    }

    public PropertyResponse getProperties(String town) {
        Pageable firstTen = PageRequest.of(0, PAGE_SIZE);
        List<Property> properties = propertyRepository.findPropertiesByTownOrderByRandom(town, firstTen);
        return ReportConverter.propertiesToResponse(town, properties);
    }

    public AgencyResponse getAgencies(String town) {
        List<Agency> agencies = agencyRepository.findAgenciesByTownOrderByRandom(town);
        return ReportConverter.agenciesToResponse(town, agencies);
    }
}
