package cat.itacademy.sprint_5_task_1_webflux.dto.response;

public record PlayerResponse(
        Long id,
        String name,
        Integer wins
) {}