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
public class OpenaiRequest {
    private String model;
    private List<Message> messages;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
