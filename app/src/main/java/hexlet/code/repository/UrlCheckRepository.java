package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import hexlet.code.util.StringUtil;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck check) throws SQLException {
        var sql = "INSERT INTO url_checks(status_code, title, h1, description, url_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, check.getStatusCode());
            stmt.setString(2, StringUtil.limitText(check.getTitle()));
            stmt.setString(3, StringUtil.limitText(check.getH1()));
            stmt.setString(4, check.getDescription());
            stmt.setLong(5, check.getUrlId());
            stmt.setTimestamp(6, check.getCreatedAt());
            stmt.executeUpdate();

            var generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static List<UrlCheck> findByUrlId(long urlId) throws SQLException {
        var sql = "SELECT id, status_code, title, h1, description, url_id, created_at " +
                "FROM url_checks WHERE url_id = ? ORDER BY created_at DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var rs = stmt.executeQuery();
            List<UrlCheck> checks = new ArrayList<>();

            while (rs.next()) {
                var id = rs.getLong("id");
                var statusCode = rs.getInt("status_code");
                var title = rs.getString("title");
                var h1 = rs.getString("h1");
                var description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");

                UrlCheck check = new UrlCheck(
                        statusCode, title, h1, description, urlId, createdAt
                );
                check.setId(id);
                checks.add(check);
            }

            return checks;
        }
    }

    public static Map<Long, UrlCheck> getLastCheckForUrls() throws SQLException {
        var sql = """
            SELECT c.*
            FROM url_checks c
            INNER JOIN (
                SELECT url_id, MAX(created_at) AS max_date
                FROM url_checks
                GROUP BY url_id
            ) latest ON c.url_id = latest.url_id AND c.created_at = latest.max_date
            """;
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var rs = stmt.executeQuery();

            Map<Long, UrlCheck> lastChecks = new HashMap<>();

            while (rs.next()) {
                var id = rs.getLong("id");
                var statusCode = rs.getInt("status_code");
                var title = rs.getString("title");
                var h1 = rs.getString("h1");
                var urlId = rs.getLong("url_id");
                var description = rs.getString("description");
                Timestamp createdAt = rs.getTimestamp("created_at");

                UrlCheck check = new UrlCheck(
                        statusCode, title, h1, description, urlId, createdAt
                );
                check.setId(id);
                lastChecks.put(check.getUrlId(), check);

            }
            return lastChecks;
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM url_checks";
        try (var conn = dataSource.getConnection();
             var stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
