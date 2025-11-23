package cat.itacademy.sprint_5_task_1_webflux.dto.response;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorResponse {
    private String message;
    private String error;
    private int status;
    private String path;
    private Instant timestamp = Instant.now();

    public ErrorResponse(String message, String error, int status, String path) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.path = path;
    }
}
