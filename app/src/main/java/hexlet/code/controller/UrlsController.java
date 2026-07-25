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

import java.net.URL;
import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        String flashType = ctx.consumeSessionAttribute("flashType");
        page.setFlash(flash);
        page.setFlashType(flashType);

        ctx.render("urls/show.jte", model("page", page));
    }

    public static void create(Context ctx) throws SQLException {
        URL url;
        try {
            var urlString = ctx.formParamAsClass("url", String.class).get();
            var uri = new URI(urlString);
            url = uri.toURL();

        } catch (Exception e) {
            var page = new MainPage();
            page.setFlash("Некорректный URL!");
            page.setFlashType("danger");

            ctx.status(422);
            ctx.render("index.jte", model("page", page));
            return;
        }

        var name = String.format("%s://%s",Objects.requireNonNull(url).getProtocol(), url.getAuthority()).toLowerCase();
        var optionalUrl = UrlRepository.findByName(name);

        if (optionalUrl.isPresent()) {
            ctx.sessionAttribute("flash", "Страница уже существует!");
            ctx.sessionAttribute("flashType", "warning");
            ctx.redirect(NamedRoutes.urlPath(optionalUrl.get().getId()));
            return;
        }

        var newUrl = new Url(name);
        UrlRepository.save(newUrl);

        ctx.sessionAttribute("flash", "Страница успешно добавлена!");
        ctx.sessionAttribute("flashType", "success");
        ctx.redirect(NamedRoutes.urlPath(newUrl.getId()));
    }
}
