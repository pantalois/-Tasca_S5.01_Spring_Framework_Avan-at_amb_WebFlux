package cat.itacademy.sprint_5_task_1_webflux.controller;

import cat.itacademy.sprint_5_task_1_webflux.domain.mongodb.GameState;
import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
    this.gameService = gameService;
    }

    @PostMapping("/game/new")
    public Mono<GameState> addGame(@RequestBody Player player) {
     return gameService.createGame(player);
    }

    @GetMapping("/game/{id}")
    public Mono<GameState> getGame(@PathVariable String id) {
        return gameService.getGameDetails(id);
    }
}
