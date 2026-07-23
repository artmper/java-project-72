package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class Url {
    @Setter
    private Long id;
    private final String name;
    private final Timestamp createdAt; // TODO: LocalDateTime или Instance
}
