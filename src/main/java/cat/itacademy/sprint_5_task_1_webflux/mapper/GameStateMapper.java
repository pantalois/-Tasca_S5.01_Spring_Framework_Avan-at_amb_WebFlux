package cat.itacademy.sprint_5_task_1_webflux.mapper;

import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.game.Move;
import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.dto.request.CreateGameRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.request.PlayMoveRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.response.GameStateResponse;
import cat.itacademy.sprint_5_task_1_webflux.exception.InvalidMoveException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GameStateMapper {

    public Player toPlayer(CreateGameRequest dto) {
        Player player = new Player();
        player.setName(dto.playerName());
        return player;
    }

    public Move toMove(PlayMoveRequest dto) {
        if (dto == null || dto.move() == null) {
            throw new InvalidMoveException("Move cannot be null");
        }
        try {
            return Move.valueOf(dto.move().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidMoveException(
                    "Invalid move: " + dto.move()
                            + ". Allowed values: " + Arrays.toString(Move.values())
            );
        }
    }

    public GameStateResponse toResponse(GameState state) {
        return new GameStateResponse(
                state.getId(),
                state.getPlayerName(),
                state.getPlayerScore(),
                state.getDealerScore(),
                state.getResult()
        );
    }
}
