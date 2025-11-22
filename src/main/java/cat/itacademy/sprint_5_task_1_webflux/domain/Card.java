package cat.itacademy.sprint_5_task_1_webflux.domain;

import lombok.Value;

@Value
public class Card {

    private final Rank rank;
    private final Suit suit;

}
