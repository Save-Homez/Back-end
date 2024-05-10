package homez.homes.dto;

import homez.homes.entity.constant.Mark;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MapResponse {
    private List<Label> labels;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Label {
        private double longitude;
        private double latitude;

        private Mark mark;
        private int avgDeposit;
        private int avgRental;
    }
}
