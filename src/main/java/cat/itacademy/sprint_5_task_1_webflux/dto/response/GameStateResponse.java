package cat.itacademy.sprint_5_task_1_webflux.dto.response;

import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameResult;

public record GameStateResponse(
        String id,
        String playerName,
        int playerScore,
        int dealerScore,
        GameResult result
) {}
