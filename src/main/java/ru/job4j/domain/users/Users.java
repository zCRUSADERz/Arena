package ru.job4j.domain.users;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;

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
     * Register user with submitted credentials.
     * @param userName user credentials.
     * @param passwordHash password hash.
     * @return true, if registered.
     */
    public final boolean register(final String userName, final byte[] passwordHash) {
        final boolean result;
        final String insert = "INSERT INTO users (name, password) VALUE (?, ?)";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(insert)) {
            statement.setString(1, userName);
            statement.setBytes(2, passwordHash);
            final int rows = statement.executeUpdate();
            result = rows == 1;
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
