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

import homez.homes.config.feign.OpenaiClient;
import homez.homes.converter.ReportConverter;
import homez.homes.dto.AgencyResponse;
import homez.homes.dto.AiReportRequest;
import homez.homes.dto.AiReportResponse;
import homez.homes.dto.AiReportResponse.Factor;
import homez.homes.dto.AiResponse.TownResult;
import homez.homes.dto.OpenaiRequest;
import homez.homes.dto.OpenaiRequest.Message;
import homez.homes.dto.OpenaiResponse;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Agency;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private static final String GPT_MODEL = "gpt-3.5-turbo";
    private static final String GPT_ROLE = "system";
    private static final String REPORT_START_STATEMENT = "서울시집 AI 분석 결과, ";
    private static final Set<String> GANGSEO_TOWN = new HashSet<>(Arrays.asList(
            "가양2동", "방화2동", "공항동", "발산1동", "방화1동", "염창동", "우장산동", "방화3동"
    )); // 강서구 공인중개사 수가 적어서 따로 처리
    private static final int PAGE_SIZE = 10;


    private final PropertyRepository propertyRepository;
    private final AgencyRepository agencyRepository;
    private final TownRepository townRepository;
    private final StationRepository stationRepository;
    private final TravelTimeRepository travelTimeRepository;
    private final AiService aiService;
    private final OpenaiClient openaiClient;

    public AiReportResponse getAiReport(AiReportRequest request, String username) {
        List<Factor> graph = getGraph(request);
        double matchRate = getMatchRate(request, username);
        String totalStatement = REPORT_START_STATEMENT + getTotalStatement(request.getTown(), request.getFactors(), graph, matchRate);

        TravelTime travelTime = travelTimeRepository.findByOriginAndDestination(request.getStation(),
                        request.getDestination())
                .orElseThrow(() -> new CustomException(DATABASE_ERROR, "DB에 해당 역 간 이동시간이 없습니다."));

        Station station = stationRepository.findOneByName(request.getStation())
                .orElseThrow(() -> new CustomException(STATION_NOT_FOUND));

        return new AiReportResponse(totalStatement, graph, matchRate, travelTime.getTime(),
                station.getName(), station.getAvgDeposit(), station.getAvgRental(), station.getAvgLump());
    }

    private List<Factor> getGraph(AiReportRequest request) {
        Town town = townRepository.findByTown(request.getTown())
                .orElseThrow(() -> new CustomException(TOWN_NOT_FOUND));
        HashMap<FactorResult, Double> factorMap = getFactorMap(town);

        List<Factor> graph = new ArrayList<>();
        List<String> inputFactors = request.getFactors();
        for (String factor : inputFactors) {
            Double percent = factorMap.get(fromName(factor));
            graph.add(new Factor(factor, (int) (percent * 100)));
        }

        graph = normalizeFactors(factorMap, new HashSet<String>(inputFactors), graph);
        return graph;
    }

    private List<Factor> normalizeFactors(HashMap<FactorResult, Double> factorMap, HashSet<String> usedFactors, List<Factor> graph) {
        if (graph.size() == 6) {
            return graph;
        }
        if (graph.size() > 6) {
            Collections.shuffle(graph);
            return graph.subList(0, 6);
        }
        return fillRandomFactors(factorMap, usedFactors, graph);
    }

    private List<Factor> fillRandomFactors(HashMap<FactorResult, Double> factorMap, HashSet<String> usedFactors, List<Factor> graph) {
        List<String> availableFactors = factorMap.keySet().stream().map(FactorResult::getName)
                .filter(name -> !usedFactors.contains(name))
                .collect(Collectors.toList());
        Collections.shuffle(availableFactors);

        while (graph.size() < 6 && !availableFactors.isEmpty()) {
            String factor = availableFactors.remove(0);
            Double percent = factorMap.get(fromName(factor));
            graph.add(new Factor(factor, (int) (percent * 100)));
        }
        return graph;
    }

    private double getMatchRate(AiReportRequest request, String username) {
        List<TownResult> aiResults = aiService.getCachedAiResponse(username).getAiResult();
        TownResult result = aiResults.stream()
                .filter(townResult -> townResult.getTown().equals(request.getTown()))
                .findFirst()
                .orElseThrow(() -> new CustomException(Ai_NOT_SUPPORTED));
        return Double.parseDouble(result.getMatchRate());
    }

    private String getTotalStatement(String town, List<String> inputFactors, List<Factor> graph, double matchRate) {
        OpenaiRequest request = getOpenaiRequest(town, inputFactors, graph, matchRate);
        OpenaiResponse response = openaiClient.generateTotalStatement(request);
        return response.getChoices().get(0).getMessage().getContent();
    }

    private OpenaiRequest getOpenaiRequest(String town, List<String> inputFactors, List<Factor> graph, double matchRate) {
        Message message = Message.builder()
                .role(GPT_ROLE)
                .content(generatePrompt(town, inputFactors, graph, matchRate))
                .build();

        return OpenaiRequest.builder()
                .model(GPT_MODEL)
                .messages(List.of(message))
                .build();
    }

    private String generatePrompt(String town, List<String> inputFactors, List<Factor> graph, double matchRate) {
        String result =  String.format("유저와 '%s'에 대한 200자 이내 짧은 총 평을 써줘. "
                        + "유저 선호 요소: %s. "
                        + "'%s' 그래프 (0~100): %s(%d), %s(%d), %s(%d), %s(%d), %s(%d), %s(%d). "
                        + "유저-'%s' 매칭률: %s%%.",
                town,
                inputFactors.toString(),
                town, graph.get(0).getName(), graph.get(0).getPercent(), graph.get(1).getName(), graph.get(1).getPercent(),
                graph.get(2).getName(), graph.get(2).getPercent(), graph.get(3).getName(), graph.get(3).getPercent(),
                graph.get(4).getName(), graph.get(4).getPercent(), graph.get(5).getName(), graph.get(5).getPercent(),
                town, matchRate);
        log.info("프롬프트: {}", result);
        return result;
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
        List<Agency> agencies = findAgencies(town);
        return ReportConverter.agenciesToResponse(town, agencies);
    }

    private List<Agency> findAgencies(String town) {
        if (GANGSEO_TOWN.contains(town)) {
            return agencyRepository.findByAddressLike("강서구");
        }

        PageRequest firstTen = PageRequest.of(0, PAGE_SIZE);
        return agencyRepository.findByTownOrderByRandom(town, firstTen);
    }
}
