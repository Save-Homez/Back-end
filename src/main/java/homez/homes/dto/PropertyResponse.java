package homez.homes.dto;

import homez.homes.entity.constant.RentType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PropertyResponse {

    private String town;
    private List<PropertyDto> properties;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PropertyDto {

        private String name;
        private String address;
        private float area;
        private int floor;
        private RentType rentType;
        private int deposit;
        private int rental;
    }
}

