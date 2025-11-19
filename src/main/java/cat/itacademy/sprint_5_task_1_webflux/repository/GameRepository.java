package cat.itacademy.sprint_5_task_1_webflux.repository;

import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<GameState, String> {
}
