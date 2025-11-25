package cat.itacademy.sprint_5_task_1_webflux.controller;

import cat.itacademy.sprint_5_task_1_webflux.dto.request.CreateGameRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.request.PlayMoveRequest;
import cat.itacademy.sprint_5_task_1_webflux.dto.response.GameStateResponse;
import cat.itacademy.sprint_5_task_1_webflux.mapper.GameStateMapper;
import cat.itacademy.sprint_5_task_1_webflux.service.GameStateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/game")
@Tag(name = "Games", description = "Gestión de partidas de Blackjack")
public class GameStateController {

    private final GameStateService gameService;
    private final GameStateMapper mapper;

    public GameStateController(GameStateService gameService,
                               GameStateMapper mapper) {
        this.gameService = gameService;
        this.mapper = mapper;
    }

    @PostMapping("/new")
    @Operation(
            summary = "Crear nueva partida",
            description = "Crea una nueva partida de Blackjack para el jugador indicado"
    )
    public Mono<ResponseEntity<GameStateResponse>> addGame(
            @RequestBody CreateGameRequest request) {
        var player = mapper.toPlayer(request);
        return gameService.createGame(player)
                .map(mapper::toResponse)
                .map(response -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response)
                );
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Consultar partida",
            description = "Obtiene el estado actual de la partida a partir de su ID"
    )
    public Mono<ResponseEntity<GameStateResponse>> getGameById(
            @PathVariable String id) {
        return gameService.getGameDetails(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}/play")
    @Operation(
            summary = "Realizar una jugada",
            description = "Permite hacer una jugada (HIT/STAND) en la partida indicada"
    )
    public Mono<ResponseEntity<GameStateResponse>> playGame(
            @PathVariable String id,
            @RequestBody PlayMoveRequest request) {
        var move = mapper.toMove(request);
        return gameService.makeMove(id, move)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}/delete")
    @Operation(
            summary = "Eliminar partida",
            description = "Elimina la partida con el ID indicado"
    )
    public Mono<ResponseEntity<Void>> deleteGame(@PathVariable String id) {
        return gameService.deleteGame(id)
                .thenReturn(ResponseEntity.noContent().build());
    }
}
