package cat.itacademy.sprint_5_task_1_webflux.repository.mogodb;

import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameState;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameStateRepository extends ReactiveMongoRepository<GameState, String> {
}
