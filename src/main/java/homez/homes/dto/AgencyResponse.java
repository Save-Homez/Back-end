package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgencyResponse {
    private String town;
    private List<AgencyDto> agencies;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AgencyDto {
        private String name;
        private String phone;
        private String address;
    }
}
