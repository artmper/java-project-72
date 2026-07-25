package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Getter
@RequiredArgsConstructor
public class Url {
    @Setter
    private Long id;
    private final String name;
    @Setter
    private Instant createdAt;
}
