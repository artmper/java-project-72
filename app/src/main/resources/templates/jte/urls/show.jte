@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <h1 class="display-6">Сайт: ${page.getUrl().getName()}</h1>
        <table class="table table-hover table-bordered mb-12" data-test="url">
            <tbody>
            <tr>
                <td>ID</td>
                <td>${page.getUrl().getId()}</td>
            </tr>
            <tr>
                <td>Имя</td>
                <td>${page.getUrl().getName()}</td>
            </tr>
            <tr>
                <td>Дата создания</td>
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
            </tbody>
        </table>

        <h2 class="fs-4 mt-5">Проверки</h2>
        <form class="mb-3" method="post" action="/urls/${page.getUrl().getId()}/checks">
            <input type="submit" value="Запустить проверку">
        </form>
        <table class="table table-hover table-bordered mb-3" data-test="checks">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Код ответа</th>
                    <th>h1</th>
                    <th>title</th>
                    <th>description</th>
                    <th>Дата добавления</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1</td>
                    <td>200</td>
                    <td>...</td>
                    <td>...</td>
                    <td>...</td>
                    <td>2024-01-01</td>
                </tr>
            </tbody>
        </table>
    `,
    flash = @`
        <div class="alert alert-success" role="alert">
            ${page.getFlash()}
        </div>
    `
)