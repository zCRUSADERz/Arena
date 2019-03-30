package ru.job4j.domain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class Users {
    private final DataSource dataSource;

    public Users(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final Optional<User> user(final String name) {
        final Optional<User> result;
        final String select = "SELECT name, password FROM users WHERE name = ?";
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(select)) {
            statement.setString(1, name);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(new RegisteredUser(
                            resultSet.getString(1),
                            resultSet.getString(2)
                    ));
                } else {
                    result = Optional.empty();
                }
            }

        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final Map<String, String> register(final User user) {
        final Map<String, String> errors;
        final String insert = "INSERT INTO users (name, password) VALUES (?, ?)";
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(insert)) {
            statement.setString(1, user.name());
            statement.setString(2, user.password());
            final int rows = statement.executeUpdate();
            if (rows != 1) {
                errors = Map.of("name", "Username is already taken");
            } else {
                errors = Collections.emptyMap();
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return errors;
    }
}
