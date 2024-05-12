package homez.homes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiRequest {
    private List<String> factors;
    @JsonProperty("destPoint")
    private String town;
    private String timeRange;
    private int sex;
    private int age;
    private int workDay;
    private int arrivalTime;
}
