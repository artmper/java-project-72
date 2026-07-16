package hexlet.code.controller;

import hexlet.code.dto.MainPage;
import hexlet.code.dto.urls.UrlPage;
import hexlet.code.dto.urls.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

import java.net.URI;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UrlsController {
    public static void index(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();

        Map<Long, UrlCheck> lastChecks = UrlCheckRepository.getLastCheckForUrls();
        var page = new UrlsPage(urls, lastChecks);

        ctx.render("urls/index.jte", model("page", page));
    }

    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));

        List<UrlCheck> checks = UrlCheckRepository.findByUrlId(id);

        var page = new UrlPage(url, checks);

        String flash = ctx.consumeSessionAttribute("flash");
        page.setFlash(flash);

        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) {
        try {
            var urlString = ctx.formParamAsClass("url", String.class).get();
            var uri = new URI(urlString);
            var url = uri.toURL();
            var name = String.format("%s://%s", url.getProtocol(), url.getAuthority()).toLowerCase();

            var optionalUrl = UrlRepository.findByName(name);
            if (optionalUrl.isPresent()) {
                ctx.sessionAttribute("flash", "Страница уже существует!");
                ctx.redirect(NamedRoutes.urlPath(optionalUrl.get().getId()));
                return;
            }

            var createdAt = new Timestamp(System.currentTimeMillis());
            var newUrl = new Url(name, createdAt);
            UrlRepository.save(newUrl);

            ctx.sessionAttribute("flash", "Страница успешно добавлена!");
            ctx.redirect(NamedRoutes.urlPath(newUrl.getId()));
        } catch (Exception e) {
            var page = new MainPage();
            page.setFlash("Некорректный URL!");

            ctx.status(422);
            ctx.render("index.jte", model("page", page));
        }
    }
}
