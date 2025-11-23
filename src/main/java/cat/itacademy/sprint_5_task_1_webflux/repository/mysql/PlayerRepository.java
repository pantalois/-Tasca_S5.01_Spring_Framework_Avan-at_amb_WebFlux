package cat.itacademy.sprint_5_task_1_webflux.repository.mysql;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository extends R2dbcRepository<Player, Long> {

    Mono<Player> findByName(String name);

    Flux<Player> findAllByOrderByWinsDesc();
}
