package ru.job4j.domain.users.auth;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.users.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class UserAuthorizationImpl implements UserAuthorization {
    private final String userName;
    private final byte[] passwordHash;
    private final ConnectionHolder connectionHolder;
    private final Users users;

    public UserAuthorizationImpl(final String userName, final byte[] passwordHash,
                             final ConnectionHolder connectionHolder,
                             final Users users) {
        this.userName = userName;
        this.passwordHash = passwordHash;
        this.connectionHolder = connectionHolder;
        this.users = users;
    }

    /**
     * Authorize user.
     * @return map with errors. If map empty, then user authorized.
     * Map(userAttribute -> error)
     */
    public final Map<String, String> authorize() {
        final Map<String, String> result;
        final String select = "SELECT password FROM users WHERE name = ? FOR UPDATE";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(select)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final byte[] password = resultSet.getBytes("password");
                    if (Arrays.equals(password, this.passwordHash)) {
                        result = Collections.emptyMap();
                    } else {
                        result = Map.of("password", "Incorrect password.");
                    }
                } else {
                    if (this.users.register(this.userName, this.passwordHash)) {
                        result = Collections.emptyMap();
                    } else {
                        result = Map.of("name", "Username is already taken");
                    }
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
