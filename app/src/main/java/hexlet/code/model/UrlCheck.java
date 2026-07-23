package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Getter
@RequiredArgsConstructor
public class UrlCheck {
    @Setter
    private Long id;
    private final String statusCode;
    private final String title;
    private final String h1;
    private final String description;
    private final Long urlId;
    private final Timestamp createdAt;
}
