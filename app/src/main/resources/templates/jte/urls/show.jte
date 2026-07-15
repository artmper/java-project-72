@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.StringUtil
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
                    <th>Дата проверки</th>
                </tr>
            </thead>
            <tbody>
                @for (var check : page.getChecks())
                <tr>
                    <td>${check.getId()}</td>
                    <td>${check.getStatusCode()}</td>
                    <td>${check.getH1()}</td>
                    <td>${check.getTitle()}</td>
                    <td>${StringUtil.limitText(check.getDescription())}</td>
                    <td>${check.getCreatedAt().toString()}</td>
                </tr>
                @endfor
            </tbody>
        </table>
    `,
    flash = @`
        <div class="alert alert-success" role="alert">
            ${page.getFlash()}
        </div>
    `
)