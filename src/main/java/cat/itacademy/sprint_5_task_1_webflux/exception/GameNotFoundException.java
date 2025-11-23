package cat.itacademy.sprint_5_task_1_webflux.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}
