package cat.itacademy.sprint_5_task_1_webflux.exception;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}
