package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.GameEngine;
import cat.itacademy.sprint_5_task_1_webflux.domain.GameResult;
import cat.itacademy.sprint_5_task_1_webflux.domain.Move;
import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.GameRecord;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.repository.GameRecordRepository;
import cat.itacademy.sprint_5_task_1_webflux.repository.GameRepository;
import cat.itacademy.sprint_5_task_1_webflux.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameService {

    // Mongo
    private final GameRepository gameRepository;

    // Motor de juego
    private final GameEngine gameEngine;

    // MySQL
    private final PlayerRepository playerRepository;
    private final GameRecordRepository gameRecordRepository;



    public Mono<GameState> createGame(Player player) {
        GameState gameState = gameEngine.initGame(player);
        return gameRepository.save(gameState);
    }

    public Mono<GameState> getGameDetails(String id) {
        return gameRepository.findById(id);
    }

    public Mono<GameState> makeMove(String id, Move move) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Game not found")))
                // 1) aplicar jugada en el motor
                .map(state ->
                        (move == Move.HIT)
                                ? gameEngine.applyHit(state)
                                : gameEngine.applyStand(state)
                )
                // 2) guardar siempre el estado en Mongo + finalizar si toca
                .flatMap(this::saveAndFinalizeIfNeeded);
    }




    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }


    private Mono<GameState> saveAndFinalizeIfNeeded(GameState state) {
        Mono<GameState> saved = gameRepository.save(state);

        if (state.getResult() == GameResult.IN_PROGRESS) {
            // La partida sigue → solo guardamos en Mongo
            return saved;
        }

        // La partida ha terminado → ranking + GameRecord en MySQL
        Mono<Void> finalize = finalizeGame(state);
        return Mono.when(saved, finalize).thenReturn(state);
    }

    // TODA la lógica de “cuando hay ganador, persisto cosas”
    private Mono<Void> finalizeGame(GameState state) {
        String playerName = state.getPlayerName();

        // 1) asegurar que el Player existe
        Mono<Player> base = playerRepository.findByName(playerName)
                .switchIfEmpty(
                        playerRepository.save(new Player(playerName, 0))
                );

        // 2) si gana el jugador, sumarle una win
        Mono<Player> updated =
                (state.getResult() == GameResult.PLAYER_WINS)
                        ? base.flatMap(player -> {
                    player.setWins(player.getWins() + 1);
                    return playerRepository.save(player);
                })
                        : base;

        // 3) guardar un GameRecord con el playerId de MySQL
        Mono<GameRecord> recordMono = updated.flatMap(player -> {
            GameRecord record = new GameRecord(
                    state.getPlayerName(),
                    state.getResult(),
                    state.getPlayerScore(),
                    state.getDealerScore()
            );

            return gameRecordRepository.save(record);
        });

        return recordMono.then();
    }


}
