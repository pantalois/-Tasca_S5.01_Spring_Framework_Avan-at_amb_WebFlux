package cat.itacademy.sprint_5_task_1_webflux.domain.mysql;


import cat.itacademy.sprint_5_task_1_webflux.domain.GameResult;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table("games")
public class GameRecord {

    @Id
    private Long id;                      // lo genera MySQL

    @NonNull
    private String playerName;

    @NonNull
    private GameResult result;            // PLAYER_WINS / DEALER_WINS / PUSH

    @NonNull
    private Integer playerScore;

    @NonNull
    private Integer dealerScore;

    private LocalDateTime createdAt;      // lo pone MySQL con DEFAULT CURRENT_TIMESTAMP
}
