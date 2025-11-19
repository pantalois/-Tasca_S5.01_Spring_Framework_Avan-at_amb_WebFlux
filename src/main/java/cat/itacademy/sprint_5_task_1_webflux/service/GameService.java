package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.GameEngine;
import cat.itacademy.sprint_5_task_1_webflux.domain.Move;
import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GameService {


    private final GameEngine gameEngine;

    private final GameRepository gameRepository;


    public GameService(GameRepository gameRepository, GameEngine gameEngine) {
        this.gameRepository = gameRepository;
        this.gameEngine = gameEngine;
    }

    public Mono<GameState> createGame(Player player) {
        GameState gameState = gameEngine.initGame(player);
        return gameRepository.save(gameState);
    }

    public Mono<GameState> getGameDetails(String id) {
        return gameRepository.findById(id);
    }

    public Mono<GameState> makeMove(String id , Move move) {
        //Lógica de que para en el caso de plantarse.
        //Si el type of play es una carta más deberíamos de llamar a una función auxiliar
        //que actualice es gameState con el nuevo estado y guardemos ese estado actualizado.
        //Necesitaré un package mapper para tener dentro un gameState mapper que me actualice mi estado de
        //la partida que encuentre por su Id.
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception()))
                // 1) LÓGICA DE NEGOCIO (GameEngine): GameState -> GameState
                .map(state ->
                        (move == Move.HIT)
                                ? gameEngine.applyHit(state)
                                : gameEngine.applyStand(state)
                )
                // 2) PERSISTENCIA (repo.save): GameState -> Mono<GameState>
                .flatMap(gameRepository::save);
    }



    public Mono<Void> deleteGame(String id) {
        return gameRepository.deleteById(id);
    }


}
