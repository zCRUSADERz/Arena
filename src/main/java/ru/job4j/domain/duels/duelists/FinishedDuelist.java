package ru.job4j.domain.duels.duelists;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class FinishedDuelist implements SimpleDuelist {
    private final String userName;
    private final int duelID;
    private final ConnectionHolder connectionHolder;

    public FinishedDuelist(final String userName, final int duelID,
                           final ConnectionHolder connectionHolder) {
        this.userName = userName;
        this.duelID = duelID;
        this.connectionHolder = connectionHolder;
    }

    /**
     * @return user name.
     */
    @Override
    public String name() {
        return this.userName;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * Includes: user damage, start_health, health.
     * @param name prepares for user.
     * @return duelist attributes.
     */
    public final Map<String, String> attributesFor(final String name) {
        final Map<String, String> result;
        final String query = ""
                + "SELECT damage, start_health, health "
                + "FROM duelists_history "
                + "WHERE user_name = ? AND duel_id = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, this.userName);
            statement.setInt(2, this.duelID);
            try (final ResultSet resultSet = statement.executeQuery()) {
                result = new DuelistAttributes(
                        this.userName, resultSet
                ).attributesFor(name);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
