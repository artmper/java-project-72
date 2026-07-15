# Page Analyzer (Анализатор страниц)

### Hexlet tests and linter status: ###

[![Actions Status](https://github.com/artmper/java-project-72/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/artmper/java-project-72/actions)

### SonarQube code analysis: ###
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=artmper_java-project-72&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=artmper_java-project-72) [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=artmper_java-project-72&metric=coverage)](https://sonarcloud.io/summary/new_code?id=artmper_java-project-72) [![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=artmper_java-project-72&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=artmper_java-project-72) [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=artmper_java-project-72&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=artmper_java-project-72)

A website that checks other pages for basic SEO health, and keeps a history of every check.

**🔗 Live demo:** [java-project-72-19s7.onrender.com](https://java-project-72-19s7.onrender.com)

## What it does

Give Page Analyzer a URL, and it fetches that page and records its HTTP status code, `<title>`, first `<h1>`, and meta description. Every check is saved, so a page's history builds up over time, and adding the same URL twice just takes you back to the existing page instead of creating a duplicate.

## Features

- Add a page by URL — only the scheme and host are stored, so `https://example.com/some/path` and `https://example.com` are treated as the same page
- Automatic duplicate detection
- Run a check at any time and see status code, title, h1, and description
- Full check history per page
- Flash messages for every action (added, already exists, invalid URL, check succeeded/failed)

## Tech stack

- Java 21, [Javalin](https://javalin.io/) for routing and request handling
- [JTE](https://jte.gg/) for server-side templates, [Bootstrap 5](https://getbootstrap.com/) for styling
- PostgreSQL in production, H2 in-memory for local dev and tests — via JDBC + HikariCP
- [Jsoup](https://jsoup.org/) to parse the fetched HTML, [Unirest](https://kong.github.io/unirest-java/) to fetch it
- JUnit 5, AssertJ, MockWebServer for tests
- Gradle (Kotlin DSL) with the Shadow plugin for the runnable jar
- SonarCloud + JaCoCo for static analysis and test coverage
- Docker for the Render deployment

## Routes

| Method | Path                 | Description                                |
|--------|----------------------|---------------------------------------------|
| GET    | `/`                  | Home page, form to add a URL                |
| GET    | `/urls`              | All tracked pages with their latest check   |
| GET    | `/urls/{id}`         | One page's details and check history        |
| POST   | `/urls`              | Add a new page                              |
| POST   | `/urls/{id}/checks`  | Run a new check on a page                   |

## How it works

```
controller/    → request handlers (Root, Urls, UrlChecks)
repository/    → JDBC data access (UrlRepository, UrlCheckRepository)
model/         → Url, UrlCheck
dto/           → objects passed into the JTE templates
templates/jte/ → views
```

Two tables back it: `urls` for tracked pages, and `url_checks` for their check history, linked by `url_id` with `ON DELETE CASCADE`.

## Running locally

Needs JDK 21. Without a `DATABASE_URL` set, the app falls back to an in-memory H2 database automatically, so there's nothing to configure to try it out.

```bash
git clone https://github.com/artmper/java-project-72.git
cd java-project-72/app
make build-run
```

The app starts on port `7070` by default (override with the `PORT` env var).

## Testing

```bash
make test     # run the test suite
make lint     # checkstyle
make report   # JaCoCo coverage report
```

## Deployment

The root `Dockerfile` builds a runnable shadow jar and starts it with `java -jar`. That's what Render.com uses to deploy the app as a Docker web service, with a Render-managed PostgreSQL database passed in through `DATABASE_URL`.