@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
    page = page,
    content = @`
        <h1>Сайт: ${page.getUrl().getName()}</h1>
        <table data-test="url">
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
    `,
    flash = @`
        <div class="alert alert-success" role="alert">
            ${page.getFlash()}
        </div>
    `
)