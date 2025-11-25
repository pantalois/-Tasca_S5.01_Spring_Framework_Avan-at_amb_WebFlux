package cat.itacademy.sprint_5_task_1_webflux.exception;

import cat.itacademy.sprint_5_task_1_webflux.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(
            GameNotFoundException ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse body = new ErrorResponse(
                ex.getMessage(),
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            WebExchangeBindException ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        String message = ex.getAllErrors().stream()
                .map(e -> e.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorResponse body = new ErrorResponse(
                message,
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(GameAlreadyFinishedException.class)
    public ResponseEntity<ErrorResponse> handleGameAlreadyFinished(
            GameAlreadyFinishedException ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse body = new ErrorResponse(
                ex.getMessage(),
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMove(
            InvalidMoveException ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse body = new ErrorResponse(
                ex.getMessage(),
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(PlayerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePlayerNotFound(
            PlayerNotFoundException ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse body = new ErrorResponse(
                ex.getMessage(),
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            ServerWebExchange exchange) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse body = new ErrorResponse(
                "Internal server error",
                status.name(),
                status.value(),
                exchange.getRequest().getPath().value()
        );

        return ResponseEntity.status(status).body(body);
    }
}
