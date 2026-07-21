package hexlet.code.controller;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import kong.unirest.Unirest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.sql.SQLException;
import java.sql.Timestamp;

public class UrlChecksController {
    public static void create(Context ctx) throws SQLException {
        var urlId = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(urlId)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + urlId + " not found"));

        try {
            var response = Unirest.get(url.getName()).asString();

            int statusCode = response.getStatus();

            if (statusCode >= 400) {
                ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
                ctx.redirect(NamedRoutes.urlPath(urlId));
                return;
            }

            String html = response.getBody();
            Document doc = Jsoup.parse(html);

            String title = doc.title();
            String h1 = doc.selectFirst("h1") != null ? doc.selectFirst("h1").text() : null;
            String description = doc.selectFirst("meta[name=description]") != null
                    ? doc.selectFirst("meta[name=description]").attr("content")
                    : null;

            Timestamp createdAt = new Timestamp(System.currentTimeMillis());
            UrlCheck check = new UrlCheck(
                    statusCode, title, h1, description, urlId, createdAt
            );
            UrlCheckRepository.save(check);

            ctx.sessionAttribute("flash", "Страница успешно проверена");
            ctx.redirect(NamedRoutes.urlPath(urlId));

        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Произошла ошибка при проверке");
            ctx.redirect(NamedRoutes.urlPath(urlId));
        }
    }
}
