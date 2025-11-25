package cat.itacademy.sprint_5_task_1_webflux.domain.record;


import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameResult;
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
    private Long id;

    @NonNull
    private String playerName;

    @NonNull
    private GameResult result;

    @NonNull
    private Integer playerScore;

    @NonNull
    private Integer dealerScore;

    private LocalDateTime createdAt;
}
