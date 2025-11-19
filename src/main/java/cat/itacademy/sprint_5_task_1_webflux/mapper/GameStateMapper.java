package cat.itacademy.sprint_5_task_1_webflux.mapper;

import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface GameStateMapper {

    void updateGameState(GameState gameState);
}
