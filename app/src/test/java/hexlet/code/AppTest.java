package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import io.javalin.http.NotFoundResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import static org.assertj.core.api.Assertions.assertThat;

public class AppTest {
    private Javalin app;

    @BeforeEach
    public final void setUp() throws IOException, SQLException {
        app = App.getApp();
        UrlRepository.removeAll();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
           var response = client.get(NamedRoutes.rootPath());

           assertThat(response.code()).isEqualTo(200);
           assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testUrlsPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());

            assertThat(response.code()).isEqualTo(200);
            String body = response.body().string();
        });
    }

    @Test
    public void testUrlPage() {
        JavalinTest.test(app, (server, client) -> {
            var url = new Url("https://io.hexlet.ru", new Timestamp(System.currentTimeMillis()));
            UrlRepository.save(url);
            var response = client.get(NamedRoutes.urlPath(url.getId()));

            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://io.hexlet.ru");
        });
    }

    @Test
    public void testUrlNotFound() throws NotFoundResponse {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath("9999999"));

            assertThat(response.code()).isEqualTo(404);
            String body = response.body().string();
            assertThat(body).contains("Entity with id = 9999999 not found");
        });
    }

    @Test
    public void testCreateUrl() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://google.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            assertThat(response.code()).isEqualTo(200);

            String body = response.body().string();
            assertThat(body).contains("https://google.com");

            var actualUrl = UrlRepository.findByName("https://google.com");
            assertThat(actualUrl).isPresent();
        });
    }

    @Test
    public void testExistingUrl() throws SQLException {
        var existingUrl = new Url("https://ya.ru", new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(existingUrl);

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://ya.ru";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            var allUrls = UrlRepository.getEntities();

            assertThat(response.code()).isEqualTo(200);
            String body = response.body().string();

            assertThat(allUrls).hasSize(1);
            assertThat(body).contains("https://ya.ru");
        });
    }

    @Test
    public void testCreateInvalidUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=not-valid-url";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);

            assertThat(response.code()).isEqualTo(422);
            assertThat(response.body().string()).contains("Некорректный URL!");

            var allUrls = UrlRepository.getEntities();
            assertThat(allUrls).isEmpty();
        });
    }
}
