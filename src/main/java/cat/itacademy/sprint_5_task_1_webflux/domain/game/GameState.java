package cat.itacademy.sprint_5_task_1_webflux.domain.game;

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

    private List<Card> playerCards;
    private List<Card> dealerCards;

    @NonNull
    private String playerName;
    @NonNull
    private Integer playerScore;

    @NonNull
    private Integer dealerScore;

    @NonNull
    private List<Card> deck;


    @NonNull
    private Turn currentTurn;

    @NonNull
    private GameResult result;
}
