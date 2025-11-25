package cat.itacademy.sprint_5_task_1_webflux.exception;

public class GameAlreadyFinishedException extends RuntimeException {
    public GameAlreadyFinishedException(String message) {
        super(message);
    }
}
