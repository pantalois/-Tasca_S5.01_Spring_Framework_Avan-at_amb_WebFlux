package cat.itacademy.sprint_5_task_1_webflux.controller;

import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.service.PlayerService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/ranking")
    public Flux<Player> ranking() {
        return playerService.getPlayersRanking();
    }

    @GetMapping("/player")
    public Flux<Player> getPlayers() {
        return playerService.getPlayers();
    }

    @PostMapping("/player")
    public Mono<Player> addPlayer(@RequestBody Player player) {
        return playerService.createPlayer(player);
    }

    @PutMapping("/player/{id}")
    public Mono<Player> updatePlayer(@PathVariable Long id, @RequestBody Player player) {
        return playerService.modifyPlayer(id, player);
    }
}
