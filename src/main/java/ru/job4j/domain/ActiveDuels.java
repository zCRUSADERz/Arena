package ru.job4j.domain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActiveDuels {
    private final DataSource dataSource;

    public ActiveDuels(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final Duel duel(final String userName) {
        final String query = ""
                + "SELECT d.created, CURRENT_TIMESTAMP() AS now "
                + "FROM users_in_duels AS u JOIN duels AS d "
                + "ON u.user_name = ? AND u.duel_id = d.id";
        final Duel result;
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new Duel(
                            resultSet.getTimestamp(1),
                            resultSet.getTimestamp(2)
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Duel for user: %s, not found.",
                            userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final boolean inDuel(final String userName) {
        final String query = ""
                + "SELECT user_name FROM users_in_duels "
                + "WHERE user_name = ?";
        final boolean result;
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next();
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
