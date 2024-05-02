package homez.homes.converter;

import homez.homes.dto.PropertyResponse;
import homez.homes.dto.PropertyResponse.PropertyDto;
import homez.homes.entity.Property;
import java.util.List;
import java.util.stream.Collectors;

public class PropertyConverter {
    public static PropertyResponse convertToResponse(String town, List<Property> properties) {
        List<PropertyDto> propertyDtos = properties.stream()
                .map(property ->
                        PropertyDto.builder()
                                .name(property.getName())
                                .address(property.getAddress())
                                .area(property.getArea())
                                .floor(property.getFloor())
                                .rentType(property.getRentType())
                                .deposit(property.getDeposit())
                                .rental(property.getRental())
                                .build()
                ).collect(Collectors.toList());
        return new PropertyResponse(town, propertyDtos);
    }
}
