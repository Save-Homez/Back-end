package homez.homes.service;

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
import static homez.homes.entity.constant.FactorResult.fromName;
import static homez.homes.response.ErrorCode.Ai_NOT_SUPPORTED;
import static homez.homes.response.ErrorCode.DATABASE_ERROR;
import static homez.homes.response.ErrorCode.STATION_NOT_FOUND;
import static homez.homes.response.ErrorCode.TOWN_NOT_FOUND;

import homez.homes.dto.AgencyResponse;
import homez.homes.dto.AiReportRequest;
import homez.homes.dto.AiReportResponse;
import homez.homes.dto.AiReportResponse.Factor;
import homez.homes.dto.AiResponse.TownResult;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Station;
import homez.homes.entity.Town;
import homez.homes.entity.TravelTime;
import homez.homes.entity.constant.FactorResult;
import homez.homes.entity.constant.RentType;
import homez.homes.repository.AgencyRepository;
import homez.homes.repository.PropertyRepository;
import homez.homes.repository.StationRepository;
import homez.homes.repository.TownRepository;
import homez.homes.repository.TravelTimeRepository;
import homez.homes.response.CustomException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

        return new AiReportResponse(totalStatement, graph, Double.parseDouble(result.getMatchRate()),
                travelTime.getTime(), station.getName(),
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
        return generatePropertyResponse();

//        Pageable firstTen = PageRequest.of(0, PAGE_SIZE);
//        List<Property> properties = propertyRepository.findPropertiesByTownOrderByRandom(town, firstTen);
//        return ReportConverter.propertiesToResponse(town, properties);
    }

    private PropertyResponse generatePropertyResponse() {
        // PropertyDto 객체 생성
        PropertyResponse.PropertyDto dto1 = PropertyResponse.PropertyDto.builder()
                .name("벽산스마트큐브")
                .area(31.12)
                .type("오피스텔")
                .floor(9)
                .rentType(RentType.LUMP)  // 이 부분은 RentType 열거형에 따라 다름
                .deposit(1200)
                .rental(0)
                .build();

        PropertyResponse.PropertyDto dto2 = PropertyResponse.PropertyDto.builder()
                .name("유림트윈파크")
                .area(19.99)
                .type("오피스텔")
                .floor(4)
                .rentType(RentType.MONTH)  // 이 부분은 RentType 열거형에 따라 다름
                .deposit(1000)
                .rental(75)
                .build();

        PropertyResponse.PropertyDto dto3 = PropertyResponse.PropertyDto.builder()
                .name("디에이치아너힐즈")
                .area(84.63)
                .type("아파트")
                .floor(18)
                .rentType(RentType.LUMP)  // 이 부분은 RentType 열거형에 따라 다름
                .deposit(92400)
                .rental(0)
                .build();

        PropertyResponse.PropertyDto dto4 = PropertyResponse.PropertyDto.builder()
                .name("해담하우스2차")
                .area(47.31)
                .type("연립다세대")
                .floor(5)
                .rentType(RentType.MONTH)  // 이 부분은 RentType 열거형에 따라 다름
                .deposit(3084)
                .rental(32)
                .build();

        PropertyResponse.PropertyDto dto5 = PropertyResponse.PropertyDto.builder()
                .name("라이프미성")
                .area(66.7)
                .type("아파트")
                .floor(11)
                .rentType(RentType.MONTH)  // 이 부분은 RentType 열거형에 따라 다름
                .deposit(5000)
                .rental(100)
                .build();

        // PropertyResponse 객체 생성
        PropertyResponse response = PropertyResponse.builder()
                .town("anything")
                .properties(Arrays.asList(dto1, dto2, dto3, dto4, dto5))
                .build();

        return response;
    }

    public AgencyResponse getAgencies(String town) {
        return generateAgencyResponse();
//        List<Agency> agencies = agencyRepository.findAgenciesByTownOrderByRandom(town);
//        return ReportConverter.agenciesToResponse(town, agencies);
    }

    private AgencyResponse generateAgencyResponse() {
        // AgencyDto 객체 생성
        AgencyResponse.AgencyDto agency1 = AgencyResponse.AgencyDto.builder()
                .name("참조은공인중개사사무소")
                .phone("02-906-9933")
                .address("서울특별시 강북구 삼양로87길 31 1층")
                .build();

        AgencyResponse.AgencyDto agency2 = AgencyResponse.AgencyDto.builder()
                .name("랜드원공인중개사사무소")
                .phone("02-999-6111")
                .address("서울특별시 강북구 수유로 61 1층")
                .build();

        AgencyResponse.AgencyDto agency3 = AgencyResponse.AgencyDto.builder()
                .name("행운공인중개사사무소")
                .phone("02-983-3759")
                .address("서울특별시 강북구 한천로155길 43 상가A동 128호")
                .build();

        AgencyResponse.AgencyDto agency4 = AgencyResponse.AgencyDto.builder()
                .name("동행부동산공인중개사사무소")
                .phone("02-996-3357")
                .address("서울특별시 강북구 수유로 65-2 102호")
                .build();

        AgencyResponse.AgencyDto agency5 = AgencyResponse.AgencyDto.builder()
                .name("수유OK부동산공인중개사사무소")
                .phone("02-930-1123")
                .address("서울특별시 강북구 수유로 32 1층")
                .build();

        AgencyResponse.AgencyDto agency6 = AgencyResponse.AgencyDto.builder()
                .name("지안공인중개사사무소")
                .phone("02-989-8949")
                .address("서울특별시 강북구 삼각산로 147 1층")
                .build();

        AgencyResponse.AgencyDto agency7 = AgencyResponse.AgencyDto.builder()
                .name("빨래골공인중개사사무소")
                .phone("02-989-8833")
                .address("서울특별시 강북구 삼양로79길 20 1층")
                .build();

        // AgencyResponse 객체 생성
        AgencyResponse response = AgencyResponse.builder()
                .town("anything")
                .agencies(Arrays.asList(agency1, agency2, agency3, agency4, agency5, agency6, agency7))
                .build();

        return response;
    }
}
