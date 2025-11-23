package cat.itacademy.sprint_5_task_1_webflux.service;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.exception.PlayerNotFoundException;
import cat.itacademy.sprint_5_task_1_webflux.repository.mysql.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Devuelve el ranking de jugadores.
     * Ahora mismo delega en findAll().
     * Si tienes un método específico en el repositorio (p.ej. findAllByOrderBySuccessRateDesc),
     * lo cambias aquí y no tocas el controller.
     */
    public Flux<Player> showRanking() {
        // Versión simple: sin ordenar
        return playerRepository.findAll();

        // Si tienes ranking en la BD, sería algo tipo:
        // return playerRepository.findAllByOrderBySuccessRateDesc();
    }

    /**
     * Actualiza el nombre del jugador por id.
     * - Si no existe: lanza PlayerNotFoundException -> 404 vía GlobalExceptionHandler.
     * - Si existe: actualiza el nombre y guarda.
     */
    public Mono<Player> modifyPlayer(Long id, Player player) {
        return playerRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new PlayerNotFoundException("Player with id " + id + " not found")
                ))
                .flatMap(existing -> {
                    existing.setName(player.getName());
                    // si luego quieres actualizar más campos, los pones aquí
                    return playerRepository.save(existing);
                });
    }
}