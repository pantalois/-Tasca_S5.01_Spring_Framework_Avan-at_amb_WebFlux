package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameEngine;
import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameResult;
import cat.itacademy.sprint_5_task_1_webflux.domain.game.Move;
import cat.itacademy.sprint_5_task_1_webflux.domain.game.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.record.GameRecord;
import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.exception.GameAlreadyFinishedException;
import cat.itacademy.sprint_5_task_1_webflux.exception.GameNotFoundException;
import cat.itacademy.sprint_5_task_1_webflux.exception.InvalidMoveException;
import cat.itacademy.sprint_5_task_1_webflux.repository.mysql.GameRecordRepository;
import cat.itacademy.sprint_5_task_1_webflux.repository.mogodb.GameStateRepository;
import cat.itacademy.sprint_5_task_1_webflux.repository.mysql.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GameStateService {

    // Mongo
    private final GameStateRepository gameRepository;

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
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + id + " not found")));
    }

    public Mono<GameState> makeMove(String id, Move move) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException("Game with id " + id + " not found")))
                .flatMap(state -> {
                    // 1) validar si la partida está terminada
                    if (state.getResult() != GameResult.IN_PROGRESS) {
                        return Mono.error(new GameAlreadyFinishedException("Game with id " + id + " is already finished"));
                    }

                    // 2) validar si el movimiento es válido
                    if (move == null) {
                        return Mono.error(new InvalidMoveException("Move cannot be null"));
                    }

                    GameState updated;

                    // 3) aplicar jugada según el tipo de movimiento
                    switch (move) {
                        case HIT -> updated = gameEngine.applyHit(state);
                        case STAND -> updated = gameEngine.applyStand(state);
                        default -> {
                            return Mono.error(new InvalidMoveException("Unsupported move: " + move));
                        }
                    }

                    // 4) guardar y finalizar si toca
                    return saveAndFinalizeIfNeeded(updated);
                });
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
