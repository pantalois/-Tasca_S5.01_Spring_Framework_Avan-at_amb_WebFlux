package cat.itacademy.sprint_5_task_1_webflux.domain;

import java.util.List;

public interface DeckFactory {

    List<Card> createShuffledDeck();
}
