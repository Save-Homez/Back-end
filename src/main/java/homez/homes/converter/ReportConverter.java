package homez.homes.converter;

import homez.homes.dto.AgencyResponse;
import homez.homes.dto.AgencyResponse.AgencyDto;
import homez.homes.dto.PropertyResponse;
import homez.homes.dto.PropertyResponse.PropertyDto;
import homez.homes.entity.Agency;
import homez.homes.entity.Property;
import java.util.List;
import java.util.stream.Collectors;

public class ReportConverter {
    public static PropertyResponse propertiesToResponse(String town, List<Property> properties) {
        List<PropertyDto> propertyDtos = properties.stream()
                .map(property ->
                        PropertyDto.builder()
                                .name(property.getName())
                                .area(property.getArea())
                                .type(property.getType())
                                .floor(property.getFloor())
                                .rentType(property.getRentType())
                                .deposit(property.getDeposit())
                                .rental(property.getRental())
                                .build()
                ).collect(Collectors.toList());
        return new PropertyResponse(town, propertyDtos);
    }

    public static AgencyResponse agenciesToResponse(String town, List<Agency> agencies) {
        List<AgencyDto> agencyDtos = agencies.stream()
                .map(agency ->
                        AgencyDto.builder()
                                .name(agency.getName())
                                .address(agency.getAddress())
                                .phone(agency.getPhone())
                                .build()
                ).collect(Collectors.toList());
        return new AgencyResponse(town, agencyDtos);
    }
}
