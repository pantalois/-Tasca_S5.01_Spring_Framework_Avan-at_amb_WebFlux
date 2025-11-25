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

    private final GameStateRepository gameRepository;

    private final GameEngine gameEngine;

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
                    if (state.getResult() != GameResult.IN_PROGRESS) {
                        return Mono.error(new GameAlreadyFinishedException("Game with id " + id + " is already finished"));
                    }

                    if (move == null) {
                        return Mono.error(new InvalidMoveException("Move cannot be null"));
                    }

                    GameState updated;

                    switch (move) {
                        case HIT -> updated = gameEngine.applyHit(state);
                        case STAND -> updated = gameEngine.applyStand(state);
                        default -> {
                            return Mono.error(new InvalidMoveException("Unsupported move: " + move));
                        }
                    }

                    return saveAndFinalizeIfNeeded(updated);
                });
    }




    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }


    private Mono<GameState> saveAndFinalizeIfNeeded(GameState state) {
        Mono<GameState> saved = gameRepository.save(state);

        if (state.getResult() == GameResult.IN_PROGRESS) {
            return saved;
        }

        Mono<Void> finalize = finalizeGame(state);
        return Mono.when(saved, finalize).thenReturn(state);
    }

    private Mono<Void> finalizeGame(GameState state) {
        String playerName = state.getPlayerName();

        Mono<Player> base = playerRepository.findByName(playerName)
                .switchIfEmpty(
                        playerRepository.save(new Player(playerName, 0))
                );

        Mono<Player> updated =
                (state.getResult() == GameResult.PLAYER_WINS)
                        ? base.flatMap(player -> {
                    player.setWins(player.getWins() + 1);
                    return playerRepository.save(player);
                })
                        : base;

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
