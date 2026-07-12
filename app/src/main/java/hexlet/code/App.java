package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws Exception {
        Javalin app = getApp();
        int port = getPort();

        app.get(NamedRoutes.rootPath(), ctx -> {
            ctx.result("Hello, World!");
        });

        app.start(port);
    }

    public static Javalin getApp() throws Exception {
        var hikariConfig = new HikariConfig();
        String jdbcUrl = getDatabaseUrl();
        hikariConfig.setJdbcUrl(jdbcUrl);

        var dataSource = new HikariDataSource(hikariConfig);
        String sql;
        try (var url = App.class.getClassLoader().getResourceAsStream("schema.sql")) {
            sql = new BufferedReader(new InputStreamReader(Objects.requireNonNull(url)))
                    .lines().collect(Collectors.joining("\n"));
        }

        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        }

        BaseRepository.dataSource = dataSource;

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(TemplateEngine.createPrecompiled(ContentType.Html)));
        });
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("DATABASE_URL",
                "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1");
    }

    private static int getPort() {
        return Integer.parseInt(System.getenv()
                .getOrDefault("PORT", "7070"));
    }
}
