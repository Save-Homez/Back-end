package homez.homes.service;

import static homez.homes.entity.constant.FactorResult.*;
import static homez.homes.entity.constant.FactorResult.AIR;
import static homez.homes.entity.constant.FactorResult.ART;
import static homez.homes.entity.constant.FactorResult.CLEAN;
import static homez.homes.entity.constant.FactorResult.CULTURE;
import static homez.homes.entity.constant.FactorResult.EDUCATION;
import static homez.homes.entity.constant.FactorResult.GREEN;
import static homez.homes.entity.constant.FactorResult.LIBRARY;
import static homez.homes.entity.constant.FactorResult.MOVIE;
import static homez.homes.entity.constant.FactorResult.NOISE;
import static homez.homes.entity.constant.FactorResult.PARK;
import static homez.homes.entity.constant.FactorResult.PARKING;
import static homez.homes.entity.constant.FactorResult.PHARMACY;
import static homez.homes.entity.constant.FactorResult.REST;
import static homez.homes.entity.constant.FactorResult.SAFETY;
import static homez.homes.entity.constant.FactorResult.WATER;
import static homez.homes.entity.constant.FactorResult.WOMEN_WELFARE;
import static homez.homes.response.ErrorCode.Ai_NOT_SUPPORTED;
import static homez.homes.response.ErrorCode.DATABASE_ERROR;
import static homez.homes.response.ErrorCode.STATION_NOT_FOUND;
import static homez.homes.response.ErrorCode.TOWN_NOT_FOUND;

import homez.homes.converter.ReportConverter;
import homez.homes.dto.AgencyResponse;
import homez.homes.dto.AiReportRequest;
import homez.homes.dto.AiReportResponse;
import homez.homes.dto.AiReportResponse.Factor;
import homez.homes.dto.AiResponse.TownResult;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Agency;
import homez.homes.entity.Property;
import homez.homes.entity.Station;
import homez.homes.entity.Town;
import homez.homes.entity.TravelTime;
import homez.homes.entity.constant.FactorResult;
import homez.homes.repository.AgencyRepository;
import homez.homes.repository.PropertyRepository;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TownRepository;
import homez.homes.repository.TravelTimeRepository;
import homez.homes.response.CustomException;
import java.util.ArrayList;
import java.util.HashMap;
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
    private final TownRepository townRepository;
    private final StationRepository stationRepository;
    private final TravelTimeRepository travelTimeRepository;
    private final AiService aiService;

    public AiReportResponse getAiReport(AiReportRequest request, String username) {
        List<TownResult> aiResults = aiService.getCachedAiResponse(username).getAiResult();
        TownResult result = aiResults.stream()
                .filter(townResult -> townResult.getTown().equals(request.getTown()))
                .findFirst()
                .orElseThrow(() -> new CustomException(Ai_NOT_SUPPORTED));

        String totalStatement = generateTotalStatement();

        Town town = townRepository.findByTown(request.getTown())
                .orElseThrow(() -> new CustomException(TOWN_NOT_FOUND));

        HashMap<FactorResult, Double> factorMap = getFactorMap(town);
        List<Factor> graph = new ArrayList<>();
        List<String> factors = request.getFactors();
        for (String factor : factors) {
            Double percent = factorMap.get(fromName(factor));
            graph.add(new Factor(factor, (int) (percent * 100)));
        }

        Station station = stationRepository.findOneByName(request.getStation())
                .orElseThrow(() -> new CustomException(STATION_NOT_FOUND));

        TravelTime travelTime = travelTimeRepository.findByOriginAndDestination(request.getStation(),
                        request.getDestination())
                .orElseThrow(() -> new CustomException(DATABASE_ERROR, "DB에 해당 역 간 이동시간이 없습니다."));

        return new AiReportResponse(totalStatement, graph, Double.parseDouble(result.getMatchRate()), travelTime.getTime(), station.getName(),
                station.getAvgDeposit(), station.getAvgRental(), station.getAvgLump());
    }

    private HashMap<FactorResult, Double> getFactorMap(Town town) {
        HashMap<FactorResult, Double> factorMap = new HashMap<>();
        factorMap.put(PHARMACY, town.getPharmacy());
        factorMap.put(WOMEN_WELFARE, town.getWomenWelfare());
        factorMap.put(EDUCATION, town.getEducation());
        factorMap.put(CULTURE, town.getCulture());
        factorMap.put(MOVIE, town.getMovie());
        factorMap.put(ART, town.getArt());
        factorMap.put(PARK, town.getPark());
        factorMap.put(LIBRARY, town.getLibrary());
        factorMap.put(GREEN, town.getGreen());
        factorMap.put(NOISE, town.getNoise());
        factorMap.put(AIR, town.getAir());
        factorMap.put(REST, town.getRest());
        factorMap.put(WATER, town.getWater());
        factorMap.put(SAFETY, town.getSafety());
        factorMap.put(CLEAN, town.getClean());
        factorMap.put(PARKING, town.getParking());
        return factorMap;
    }

    private String generateTotalStatement() {
        return "AI 종합 분석 준비 중 입니다.";
    }

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
