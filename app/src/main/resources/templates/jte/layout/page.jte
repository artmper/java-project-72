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
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-3">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">Анализатор страниц</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Переключатель навигации">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav">
                        <li class="nav-item">
                            <a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
                        </li>
                    </ul>
                </div>
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