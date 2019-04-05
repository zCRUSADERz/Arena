package ru.job4j.domain.duels.activity;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBLastActivity implements Activity {
    private final String userName;
    private final ConnectionHolder connectionHolder;

    public DBLastActivity(final String userName, final ConnectionHolder connectionHolder) {
        this.userName = userName;
        this.connectionHolder = connectionHolder;
    }

    @Override
    public final long activity() {
        final long result;
        final String query = ""
                + "SELECT last_activity FROM active_duelists "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
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
    public final void update(final double delay) {
        final String query = ""
                + "UPDATE active_duelists "
                + "SET last_activity = CURRENT_TIMESTAMP(3) + ? "
                + "WHERE user_name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setDouble(1, delay);
            statement.setString(2, this.userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Failed to update the last activity date for: %s.",
                        this.userName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
