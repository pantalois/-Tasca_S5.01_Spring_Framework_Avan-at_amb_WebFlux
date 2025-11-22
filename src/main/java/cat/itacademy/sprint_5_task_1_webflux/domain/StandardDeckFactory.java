package cat.itacademy.sprint_5_task_1_webflux.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import java.security.SecureRandom;

@Component
public class StandardDeckFactory implements DeckFactory {

    private final Random random = new SecureRandom();

    @Override
    public List<Card> createShuffledDeck() {
        List<Card> deck =
                Arrays.stream(Suit.values())
                        .flatMap(suit ->
                                Arrays.stream(Rank.values())
                                        .map(rank -> new Card(rank, suit))
                        )
                        .collect(Collectors.toList());

        Collections.shuffle(deck, random);
        return deck;
    }
}
