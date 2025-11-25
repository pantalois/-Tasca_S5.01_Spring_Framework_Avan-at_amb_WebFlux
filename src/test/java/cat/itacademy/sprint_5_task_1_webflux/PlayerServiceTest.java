package cat.itacademy.sprint_5_task_1_webflux;

import cat.itacademy.sprint_5_task_1_webflux.domain.player.Player;
import cat.itacademy.sprint_5_task_1_webflux.exception.PlayerNotFoundException;
import cat.itacademy.sprint_5_task_1_webflux.repository.mysql.PlayerRepository;
import cat.itacademy.sprint_5_task_1_webflux.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void getRanking_whenThereArePlayers_returnsFluxOfPlayers() {
        // given
        Player p1 = new Player();
        p1.setId(1L);
        p1.setName("Alice");

        Player p2 = new Player();
        p2.setId(2L);
        p2.setName("Bob");

        when(playerRepository.findAll()).thenReturn(Flux.fromIterable(List.of(p1, p2)));

        Flux<Player> result = playerService.showRanking();


        StepVerifier.create(result)
                .expectNext(p1)
                .expectNext(p2)
                .verifyComplete();

        verify(playerRepository, times(1)).findAll();
    }

    @Test
    void modifyPlayer_whenPlayerExists_updatesNameAndReturnsUpdatedPlayer() {
        Long id = 1L;

        Player existing = new Player();
        existing.setId(id);
        existing.setName("OldName");

        Player updateData = new Player();
        updateData.setName("NewName");

        Player saved = new Player();
        saved.setId(id);
        saved.setName("NewName");

        when(playerRepository.findById(id)).thenReturn(Mono.just(existing));
        when(playerRepository.save(any(Player.class))).thenReturn(Mono.just(saved));


        Mono<Player> result = playerService.modifyPlayer(id, updateData);


        StepVerifier.create(result)
                .expectNextMatches(player ->
                        player.getId() == id &&
                                "NewName".equals(player.getName())
                )
                .verifyComplete();

        verify(playerRepository, times(1)).findById(id);
        verify(playerRepository, times(1)).save(any(Player.class));
    }

    @Test
    void modifyPlayer_whenPlayerDoesNotExist_throwsPlayerNotFoundException() {

        Long id = 99L;
        Player updateData = new Player();
        updateData.setName("Whatever");

        when(playerRepository.findById(id)).thenReturn(Mono.empty());


        Mono<Player> result = playerService.modifyPlayer(id, updateData);


        StepVerifier.create(result)
                .expectErrorMatches(throwable ->
                        throwable instanceof PlayerNotFoundException &&
                                throwable.getMessage().equals("Player with id " + id + " not found")
                )
                .verify();

        verify(playerRepository, times(1)).findById(id);
        verify(playerRepository, never()).save(any(Player.class));
    }
}
