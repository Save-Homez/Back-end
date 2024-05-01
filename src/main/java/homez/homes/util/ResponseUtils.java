package homez.homes.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import homez.homes.response.Response;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ResponseUtils() {}

    public static void sendErrorResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        Response<Void> body = Response.error(message);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(body);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    public static <T> void sendSuccessResponse(HttpServletResponse response, int statusCode, T data) throws IOException {
        response.setStatus(statusCode);
        Response<T> body = Response.success(data);
        response.setContentType("application/json;charset=UTF-8");
        String jsonResponse = objectMapper.writeValueAsString(body);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
