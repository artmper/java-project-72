@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
    content = @`
        <h1 class="display-3 mb-3">Сайты</h1>
        <table class="table table-hover table-bordered" data-test="urls">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Имя</th>
                <th scope="col">Дата создания</th>
                <th scope="col">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @if (page != null && page.getUrls() != null)
                @for (var url : page.getUrls())
                    <tr>
                        <th scope="row">${url.getId()}</th>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>${url.getCreatedAt().toString()}</td>
                        <td></td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    `
)
