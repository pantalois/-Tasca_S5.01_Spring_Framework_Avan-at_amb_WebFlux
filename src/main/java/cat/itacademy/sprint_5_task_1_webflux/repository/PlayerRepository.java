package cat.itacademy.sprint_5_task_1_webflux.repository;

import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PlayerRepository extends R2dbcRepository<Player, Long> {
}
