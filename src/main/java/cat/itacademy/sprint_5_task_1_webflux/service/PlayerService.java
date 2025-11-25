package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.exception.PlayerNotFoundException;
import cat.itacademy.sprint_5_task_1_webflux.repository.mysql.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    public Flux<Player> showRanking() {
        return playerRepository.findAllByOrderByWinsDesc();
    }


    public Mono<Player> modifyPlayer(Long id, Player player) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new PlayerNotFoundException("Player with id " + id + " not found")
                ))
                .flatMap(existing -> {
                    existing.setName(player.getName());
                    return playerRepository.save(existing);
                });
    }
}