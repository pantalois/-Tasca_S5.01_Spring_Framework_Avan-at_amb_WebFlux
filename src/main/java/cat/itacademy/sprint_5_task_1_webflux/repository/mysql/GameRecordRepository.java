package cat.itacademy.sprint_5_task_1_webflux.repository.mysql;

import cat.itacademy.sprint_5_task_1_webflux.domain.record.GameRecord;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface GameRecordRepository extends R2dbcRepository<GameRecord, Long> {
}