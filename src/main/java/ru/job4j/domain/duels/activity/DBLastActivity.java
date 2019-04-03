package ru.job4j.domain.duels.activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBLastActivity implements Activity {
    private final String userName;
    private final Connection connection;

    public DBLastActivity(final String userName, final Connection connection) {
        this.userName = userName;
        this.connection = connection;
    }

    @Override
    public final long activity() {
        final long result;
        final String query = ""
                + "SELECT last_activity FROM users_in_duels "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet
                            .getTimestamp("last_activity")
                            .getTime();
                } else {
                    throw new IllegalStateException(String.format(
                            "User: %s not exist in active duels.",
                            this.userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    @Override
    public final void update(final double delay) throws SQLException {
        final String query = ""
                + "UPDATE users_in_duels "
                + "SET last_activity = CURRENT_TIMESTAMP(3) + ? "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setDouble(1, delay);
            statement.setString(2, this.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Failed to update the last activity date for: %s.",
                        this.userName
                ));
            }
        }
    }
}
