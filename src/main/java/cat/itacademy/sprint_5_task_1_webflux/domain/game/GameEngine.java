package cat.itacademy.sprint_5_task_1_webflux.domain.game;


import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameEngine {

    private final DeckFactory deckFactory;

    public GameEngine(DeckFactory deckFactory) {
        this.deckFactory = deckFactory;
    }

    public GameState initGame(Player player) {

        List<Card> deck = deckFactory.createShuffledDeck();


        GameState state = new GameState(
                player.getName(),
                0,
                0,
                deck,
                Turn.PLAYER,
                GameResult.IN_PROGRESS
        );


        dealInitialCards(state);


        return state;
    }

    private void dealInitialCards(GameState state) {
        List<Card> deck = state.getDeck();

        List<Card> playerCards = new java.util.ArrayList<>();
        List<Card> dealerCards = new java.util.ArrayList<>();


        playerCards.add(draw(deck));
        dealerCards.add(draw(deck));
        playerCards.add(draw(deck));
        dealerCards.add(draw(deck));

        state.setPlayerCards(playerCards);
        state.setDealerCards(dealerCards);

       state.setPlayerScore(calculateScore(playerCards));
       state.setDealerScore(calculateScore(dealerCards));
    }

    private Card draw(List<Card> deck) {
        return deck.remove(0);
    }

    private GameState drawCardForPlayer(GameState state) {
        Card card = draw(state.getDeck());
        state.getPlayerCards().add(card);
        state.setPlayerScore(calculateScore(state.getPlayerCards()));
        return state;
    }

    private GameState drawCardForDealer(GameState state) {
        Card card = draw(state.getDeck());
        state.getDealerCards().add(card);
        state.setDealerScore(calculateScore(state.getDealerCards()));
        return state;
    }



    public int calculateScore(List<Card> hand) {
        int total = hand.stream()
                .map(Card::getRank)
                .mapToInt(Rank::getValue)
                .sum();

        long aces = hand.stream()
                .map(Card::getRank)
                .filter(rank -> rank == Rank.ACE)
                .count();

        if (aces > 0 && total + 10 <= 21) {
            total += 10;
        }

        return total;
    }



    public GameState applyHit(GameState state) {

        if (state.getResult() != GameResult.IN_PROGRESS) {
            return state;
        }


        if (state.getCurrentTurn() == Turn.PLAYER) {
            drawCardForPlayer(state);


            if (state.getPlayerScore() > 21) {
                state.setResult(GameResult.DEALER_WINS);
                return state;
            }


            return (state.getPlayerScore() == 21)
                    ? applyStand(state)
                    : state;
        }

        drawCardForDealer(state);
        if (state.getDealerScore() > 21) {
            state.setResult(GameResult.PLAYER_WINS);
        }
        return state;
    }


    private void decideWinner(GameState state) {
        int player = state.getPlayerScore();
        int dealer = state.getDealerScore();

        GameResult result =
                (dealer > 21)       ? GameResult.PLAYER_WINS :
                (player > 21)       ? GameResult.DEALER_WINS :
                (dealer > player)   ? GameResult.DEALER_WINS :
                (dealer < player)   ? GameResult.PLAYER_WINS :
                GameResult.PUSH;

        state.setResult(result);
    }

    public GameState applyStand(GameState state) {
        if (state.getResult() != GameResult.IN_PROGRESS) {
            return state;
        }

        if (state.getCurrentTurn() != Turn.PLAYER) {
            return state;
        }

        state.setCurrentTurn(Turn.DEALER);

        while (state.getDealerScore() < 17 || state.getDealerScore() < state.getPlayerScore()) {
            drawCardForDealer(state);
        }

        decideWinner(state);

        return state;
    }
}