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
        // 1) Creamos el mazo barajado
        List<Card> deck = deckFactory.createShuffledDeck();

        // 2) Creamos la instancia base de GameState
        GameState state = new GameState(
                player.getName(),
                0,      // playerScore inicial
                0,      // dealerScore inicial
                deck,  // mazo completo, aún sin robar
                Turn.PLAYER,
                GameResult.IN_PROGRESS
        );

        // 3) Repartimos cartas modificando ESA MISMA instancia
        dealInitialCards(state);

        // 4) Devolvemos el mismo objeto, ya preparado
        return state;
    }

    private void dealInitialCards(GameState state) {
        List<Card> deck = state.getDeck();

        List<Card> playerCards = new java.util.ArrayList<>();
        List<Card> dealerCards = new java.util.ArrayList<>();

        // formato que tú uses para las cartas, por ejemplo "AS_CORAZONES"
        playerCards.add(draw(deck));
        dealerCards.add(draw(deck)); // crupier boca arriba
        playerCards.add(draw(deck));
        dealerCards.add(draw(deck)); // crupier boca abajo

        state.setPlayerCards(playerCards);
        state.setDealerCards(dealerCards);

       state.setPlayerScore(calculateScore(playerCards));
       state.setDealerScore(calculateScore(dealerCards)); // o solo la visible, como tú decidas
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
        // Si la partida ya ha terminado, no hacemos nada
        if (state.getResult() != GameResult.IN_PROGRESS) {
            return state;
        }

        // Si es turno del jugador
        if (state.getCurrentTurn() == Turn.PLAYER) {
            drawCardForPlayer(state); // roba, recalcula playerScore

            // Se pasa → gana la casa
            if (state.getPlayerScore() > 21) {
                state.setResult(GameResult.DEALER_WINS);
                return state;
            }

            // Clava 21 → auto-stand: cede turno al crupier
            return (state.getPlayerScore() == 21)
                    ? applyStand(state)   // aquí el crupier juega su parte y decideWinner
                    : state;              // si no, sigue jugando normalmente
        }

        // Si algún día permites HIT del crupier manualmente:
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
        // si ya no está en juego, no hacemos nada
        if (state.getResult() != GameResult.IN_PROGRESS) {
            return state;
        }

        // solo tiene sentido si es turno del jugador
        if (state.getCurrentTurn() != Turn.PLAYER) {
            return state;
        }

        // 1) el jugador pasa turno al crupier
        state.setCurrentTurn(Turn.DEALER);

        // 2) el crupier roba hasta tener 17 o más
        while (state.getDealerScore() < 17 || state.getDealerScore() < state.getPlayerScore()) {
            drawCardForDealer(state);
        }

        // 3) decidir ganador con las puntuaciones finales
        decideWinner(state);

        return state;
    }
}