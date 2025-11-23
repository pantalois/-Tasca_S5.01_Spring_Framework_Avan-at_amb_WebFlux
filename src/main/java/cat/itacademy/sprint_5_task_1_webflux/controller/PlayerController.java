package cat.itacademy.sprint_5_task_1_webflux.controller;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.dto.request.PlayerRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.response.PlayerResponse;
import cat.itacademy.sprint_5_task_1_webflux.mapper.PlayerMapper;
import cat.itacademy.sprint_5_task_1_webflux.service.PlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Players", description = "Operaciones relacionadas con jugadores")
public class PlayerController {

    private final PlayerService playerService;
    private final PlayerMapper playerMapper;

    public PlayerController(PlayerService playerService,
                            PlayerMapper playerMapper) {
        this.playerService = playerService;
        this.playerMapper = playerMapper;
    }

    @GetMapping("/ranking")
    @Operation(
            summary = "Obtener ranking de jugadores",
            description = "Devuelve el ranking de jugadores ordenados según el criterio definido en PlayerService"
    )
    public Flux<PlayerResponse> getRanking() {
        return playerService.showRanking()
                .map(playerMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar jugador por ID",
            description = "Actualiza el nombre de un jugador existente identificado por su ID"
    )
    public Mono<ResponseEntity<PlayerResponse>> updatePlayer(
            @PathVariable Long id,
            @RequestBody PlayerRequest request) {

        Player player = playerMapper.toEntity(request);

        return playerService.modifyPlayer(id, player)
                .map(playerMapper::toResponse)
                .map(ResponseEntity::ok);
    }
}
