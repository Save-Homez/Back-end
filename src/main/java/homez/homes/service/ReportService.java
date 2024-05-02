package homez.homes.service;

import homez.homes.converter.PropertyConverter;
import homez.homes.dto.PropertyResponse;
import homez.homes.entity.Property;
import homez.homes.repository.PropertyRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReportService {
    private final int PAGE_SIZE = 10;

    private final PropertyRepository propertyRepository;

    public PropertyResponse getProperties(String town) {
        Pageable firstTen = PageRequest.of(0, PAGE_SIZE);
        List<Property> properties = propertyRepository.findByTownOrderByRandom(town, firstTen);
        return PropertyConverter.convertToResponse(town, properties);
    }


}
