package ru.job4j.domain.duels.logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttackLogs {
    private final Connection connection;

    public AttackLogs(final Connection connection) {
        this.connection = connection;
    }

    public final void create(final String attackerName, final int duelId,
                             final String targetName) {
        final String query = ""
                + "INSERT INTO attack_log (attacker_name, duel_id, target_name) "
                + "VALUES (?, ?, ?);";
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, attackerName);
            statement.setInt(2, duelId);
            statement.setString(3, targetName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error creating a record in the battle log."
                                + "Duel: %s, attacker: %s, target: %s.",
                        duelId, attackerName, targetName
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
