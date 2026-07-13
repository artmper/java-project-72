package hexlet.code.repository;

import com.zaxxer.hikari.HikariDataSource;

public class BaseRepository {
    private BaseRepository() { }

    public static HikariDataSource dataSource;
}
