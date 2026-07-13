@import gg.jte.Content
@import hexlet.code.util.NamedRoutes

@param String title
@param String description
@param Content content
@param Content flash = null

<!doctype html>
<html lang="ru">
    <head>
        <meta charset="utf-8" />
        @if(description != null)
            <meta name="description" content="${description}">
        @endif
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css"
              rel="stylesheet"
              integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB"
              crossorigin="anonymous">
        <title>${title}</title>
    </head>
    <body>
        <nav> </nav>
        <main class="container">
            <div class="content">
                ${content}
            </div>
        </main>
    </body>
</html>