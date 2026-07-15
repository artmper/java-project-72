package hexlet.code.controller;

import hexlet.code.App;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlChecksControllerTest {

    private static MockWebServer mockServer;
    private static String baseUrl;
    private Javalin app;

    @BeforeAll
    static void setUpMock() throws IOException {
        mockServer = new MockWebServer();
        mockServer.start();
        baseUrl = mockServer.url("/").toString();
    }

    @AfterAll
    static void closeMock() throws IOException {
        mockServer.shutdown();
    }

    @BeforeEach
    void init() throws IOException, SQLException {
        app = App.getApp();
        UrlCheckRepository.removeAll();
        UrlRepository.removeAll();

        Url url = new Url(baseUrl, new Timestamp(System.currentTimeMillis()));
        UrlRepository.save(url);
    }

    @Test
    void testCheckSuccess() throws SQLException {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test H1</h1>" +
                "<meta name=\"description\" content=\"Test Description\"></body></html>";
        mockServer.enqueue(new MockResponse().setBody(html).setResponseCode(200));

        JavalinTest.test(app, (server, client) -> {
            long urlId = UrlRepository.findByName(baseUrl).orElseThrow().getId();
            var response = client.post(NamedRoutes.checkUrlPath(urlId), "");

            assertThat(response.code()).isEqualTo(200);

            var checks = UrlCheckRepository.findByUrlId(urlId);
            assertThat(checks).hasSize(1);
            UrlCheck check = checks.get(0);
            assertThat(check.getStatusCode()).isEqualTo(200);
            assertThat(check.getTitle()).isEqualTo("Test Title");
            assertThat(check.getH1()).isEqualTo("Test H1");
            assertThat(check.getDescription()).isEqualTo("Test Description");
        });
    }

    @Test
    void testCheckErrorResponse() throws SQLException {
        mockServer.enqueue(new MockResponse().setResponseCode(404));

        JavalinTest.test(app, (server, client) -> {
            long urlId = UrlRepository.findByName(baseUrl).orElseThrow().getId();
            var response = client.post(NamedRoutes.checkUrlPath(urlId), "");

            assertThat(response.code()).isEqualTo(200);

            var checks = UrlCheckRepository.findByUrlId(urlId);
            assertThat(checks).isEmpty();
        });
    }

    @Test
    void testCheckTags() throws SQLException {
        String html = "<html><head></head><body></body></html>";
        mockServer.enqueue(new MockResponse().setBody(html).setResponseCode(200));

        JavalinTest.test(app, (server, client) -> {
            long urlId = UrlRepository.findByName(baseUrl).orElseThrow().getId();
            client.post(NamedRoutes.checkUrlPath(urlId), "");

            var checks = UrlCheckRepository.findByUrlId(urlId);
            assertThat(checks).hasSize(1);
            UrlCheck check = checks.get(0);
            assertThat(check.getTitle()).isNullOrEmpty();
            assertThat(check.getH1()).isNullOrEmpty();
            assertThat(check.getDescription()).isNullOrEmpty();
        });
    }
}