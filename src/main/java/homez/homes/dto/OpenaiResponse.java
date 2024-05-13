package homez.homes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OpenaiResponse {
    private List<Choice> choices;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Choice {
        private Message message;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Message {
        private String content;
    }
}
