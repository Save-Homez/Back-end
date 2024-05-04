package homez.homes.service;

import homez.homes.converter.ReportConverter;
import homez.homes.dto.AgencyResponse;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Agency;
import homez.homes.entity.Property;
import homez.homes.repository.AgencyRepository;
import homez.homes.repository.PropertyRepository;
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
