package cat.itacademy.sprint_5_task_1_webflux.domain;


import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import org.springframework.stereotype.Component;

@Component
public class GameEngine {

    public GameState initGame(Player player) {
        return new GameState(player.getName(), 0, 0);
    }

    public GameState applyHit(GameState gameState) {
        gameState.setPlayerScore(gameState.getPlayerScore() + 1);
        return gameState;
    }

    public GameState applyStand(GameState gameState) {
        gameState.setPlayerScore(gameState.getPlayerScore() - 1);
        return gameState;
    }


}
