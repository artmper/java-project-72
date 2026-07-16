@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
    content = @`
        <h1 class="display-4 mb-3">Все сайты</h1>
        <table class="table table-hover table-bordered" data-test="urls">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Имя</th>
                <th scope="col">Дата проверки</th>
                <th scope="col">Код ответа</th>
            </tr>
            </thead>
            <tbody>
            @if (page != null && page.getUrls() != null)
                @for (var url : page.getUrls())
                    <tr>
                        <th scope="row">${url.getId()}</th>
                        <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                        <td>
                            !{var lastCheck = page.getLastChecks().get(url.getId());}
                            @if (lastCheck != null)
                                ${lastCheck.getCreatedAt().toString()}
                            @endif
                        </td>
                        <td>
                            @if (lastCheck != null)
                                ${lastCheck.getStatusCode()}
                            @endif
                        </td>
                    </tr>
                @endfor
            @endif
            </tbody>
        </table>
    `
)
