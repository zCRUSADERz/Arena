package ru.job4j.domain.users;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class Users {
    private final ConnectionHolder connectionHolder;

    public Users(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public final AuthenticationResult authorize(
            final UserCredentials userCredentials) {
        final AuthenticationResult result;
        final String select = "SELECT password FROM users WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(select)) {
            statement.setString(1, userCredentials.name());
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final byte[] password = resultSet.getBytes("password");
                   if (Arrays.equals(password, userCredentials.password())) {
                       result = new AuthenticationResult(true, false);
                   } else {
                       result = new AuthenticationResult(true, true);
                   }
                } else {
                    result = new AuthenticationResult(false, false);
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final boolean register(final UserCredentials user) {
        final boolean result;
        final String insert = "INSERT INTO users (name, password) VALUES (?, ?)";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(insert)) {
            statement.setString(1, user.name());
            statement.setBytes(2, user.password());
            final int rows = statement.executeUpdate();
            result = rows != 1;
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
