package cat.itacademy.sprint_5_task_1_webflux.domain.mysql;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table("crupiers")
public class Crupier {

    @Id
    private long id;

    @NonNull
    private String name;
}
