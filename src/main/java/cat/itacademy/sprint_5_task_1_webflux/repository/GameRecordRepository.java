package cat.itacademy.sprint_5_task_1_webflux.repository;

import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.GameRecord;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface GameRecordRepository extends R2dbcRepository<GameRecord, Long> {

    // si luego quieres ver las partidas de un jugador concreto:
    Flux<GameRecord> findByPlayerNameOrderByIdDesc(String playerName);
}