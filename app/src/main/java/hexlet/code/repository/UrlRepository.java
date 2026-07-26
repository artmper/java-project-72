package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        String sql = "INSERT INTO urls (name) VALUES (?)";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql, new String[]{"id", "created_at"})) {
            stmt.setString(1, url.getName());
            stmt.executeUpdate();
            var generatedKeys = stmt.getGeneratedKeys();

            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong("id"));
                url.setCreatedAt(generatedKeys.getTimestamp("created_at").toInstant());
            } else {
                throw new SQLException("DB have not returned an id after saving an entity");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toInstant();
                var url = new Url(name);
                url.setCreatedAt(createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String name) throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls WHERE name = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            var resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var resName = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toInstant();
                var url = new Url(resName);
                url.setCreatedAt(createdAt);
                url.setId(id);
                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        var sql = "SELECT id, name, created_at FROM urls ORDER BY id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var entities = new ArrayList<Url>();

            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at").toInstant();
                var url = new Url(name);
                url.setCreatedAt(createdAt);
                url.setId(id);
                entities.add(url);
            }
            return entities;
        }
    }

    public static void removeAll() throws SQLException {
        var sql = "DELETE FROM urls";
        try (var conn = dataSource.getConnection();
                var stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }
}
