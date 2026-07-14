@import gg.jte.Content
@import hexlet.code.dto.BasePage
@import hexlet.code.util.NamedRoutes

@param Content content
@param Content flash = null
@param BasePage page = null

<!doctype html>
<html lang="ru">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous">
        <title>PageAnalyzer</title>
    </head>
    <body>
        <nav class="navbar navbar-dark bg-dark mb-3">
            <div class="container-fluid d-flex justify-content-start align-items-center">
                <a class="navbar-brand " href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
                <ul class="navbar-nav d-flex flex-row gap-3">
                    <li class="nav-item">
                        <a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${NamedRoutes.urlsPath()}">Все сайты</a>
                    </li>
                </ul>
            </div>
        </nav>
        <main class="container">
            <div class="content">
                @if(page != null && page.getFlash() != null)
                    <p>${flash}</p>
                @endif
                ${content}
            </div>
        </main>
    </body>
</html>