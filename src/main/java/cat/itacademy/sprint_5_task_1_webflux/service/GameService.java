package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameService {

    private final GameRepository gameRepository;

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
    public Mono<GameState> createGame(Player player) {
        GameState gameState = initGame(player);
        return gameRepository.save(gameState);
    }

    private GameState initGame(Player player) {
        return new GameState(player.getName(), 0, 0);
    }
}
