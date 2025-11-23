package cat.itacademy.sprint_5_task_1_webflux.mapper;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.dto.request.PlayerRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.response.PlayerResponse;
import org.springframework.stereotype.Component;

@Component
public class PlayerMapper {

    public Player toEntity(PlayerRequest dto) {
        Player player = new Player();
        player.setName(dto.name());
        return player;
    }

    public PlayerResponse toResponse(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getWins()
        );
    }
}
