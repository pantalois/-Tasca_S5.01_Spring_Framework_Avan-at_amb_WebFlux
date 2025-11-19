package cat.itacademy.sprint_5_task_1_webflux.domain.mysql;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table("players")
public class Player {

    @Id
    private long id;

    @NonNull
    private String name;
}
