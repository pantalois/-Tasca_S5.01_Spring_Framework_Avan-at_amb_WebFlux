package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.mysql.Player;
import cat.itacademy.sprint_5_task_1_webflux.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Mono<Player> registerWin(String playerName) {
        return playerRepository.findByName(playerName)
                .switchIfEmpty(
                        Mono.defer(() -> playerRepository.save(
                                new Player(playerName, 0)
                        ))
                )
                .flatMap(player -> {
                    player.setWins(player.getWins() + 1);
                    return playerRepository.save(player);
                });
    }

    public Flux<Player> showRanking() {
        return playerRepository.findAllByOrderByWinsDesc();
    }


    public Mono<Player> createPlayer(Player player) {
        return playerRepository.save(player); //Comprobar que no exista el nombre en base de datos
    }

    public Flux<Player> getPlayers(){
        return playerRepository.findAll();
    }

    public Mono<Player> modifyPlayer(Long id , Player player) {
        return playerRepository.findById(id)
                .flatMap(playerModified -> {
                    playerModified.setName(player.getName());
                    return playerRepository.save(playerModified);
                });
    }
}
