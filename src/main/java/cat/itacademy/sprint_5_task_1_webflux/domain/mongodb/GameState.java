package cat.itacademy.sprint_5_task_1_webflux.domain.mongodb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@RequiredArgsConstructor
@Data
@Document(collection = "game_states")
public class GameState {

    @Id
    private String id;
    private Long gameId;

    private List<String> playerCards;
    private List<String> dealerCards;

    @NonNull
    private String playerName;
    @NonNull
    private Integer playerScore;

    @NonNull
    private Integer dealerScore;

    private List<String> deck; // mazo que se va reduciendo
}
