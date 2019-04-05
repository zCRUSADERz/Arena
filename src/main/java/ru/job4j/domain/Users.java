package ru.job4j.domain;

import ru.job4j.domain.duels.logs.results.AttackResult;

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
                            resultSet.getString("name"),
                            resultSet.getBytes("password")
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
            statement.setBytes(2, user.password());
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

    public final void upgrade(final AttackResult attackResult) {
        this.upgrade(attackResult.attacker(), 1);
        this.upgrade(attackResult.target(), -1);
    }

    @Override
    public final void close() throws Exception {
        this.connectionFactory.get().close();
    }

    private void upgrade(final String userName, final int rating) {
        final String upgrade = ""
                + "UPDATE users  "
                + "SET health = health + 1, damage = damage + 1, rating = rating + ? "
                + "WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(upgrade)) {
            statement.setInt(1, rating);
            statement.setString(2, userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Upgrade error for user: %s.",
                        userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
