package ru.job4j.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Users implements AutoCloseable {
    private final Supplier<Connection> connectionFactory;

    public Users(final Supplier<Connection> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public final Optional<User> user(final String name) {
        final Optional<User> result;
        final String select = "SELECT name, password FROM users WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(select)) {
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
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(insert)) {
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

    public final void upgrade(final String userName) {
        final String insert = ""
                + "UPDATE users  "
                + "SET health = health + 1, damage = damage + 1 "
                + "WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(insert)) {
            statement.setString(1, userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "User: %s, not found. Upgrade error.",
                        userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void close() throws Exception {
        this.connectionFactory.get().close();
    }
}
