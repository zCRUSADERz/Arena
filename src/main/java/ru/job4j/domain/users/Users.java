package ru.job4j.domain.users;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.users.auth.AuthenticationResult;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Users.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class Users {
    private final ConnectionHolder connectionHolder;

    public Users(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    /**
     * Authorize user by credentials submitted.
     * @param userCredentials user credentials.
     * @return authentication result. Registered user or not, as well
     * as the result of verification of credentials if registered.
     */
    public final AuthenticationResult authorize(
            final UserCredentials userCredentials) {
        final AuthenticationResult result;
        final String select = "SELECT password FROM users WHERE name = ? FOR UPDATE";
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

    /**
     * Register user with submitted credentials.
     * @param user user credentials.
     * @return true, if registered.
     */
    public final boolean register(final UserCredentials user) {
        final boolean result;
        final String insert = "INSERT INTO users (name, password) VALUE (?, ?)";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(insert)) {
            statement.setString(1, user.name());
            statement.setBytes(2, user.password());
            final int rows = statement.executeUpdate();
            result = rows == 1;
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
