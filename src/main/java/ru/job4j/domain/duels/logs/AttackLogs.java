package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.factories.LogsFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AttackLogs implements LogsFactory {
    private final ConnectionHolder connectionHolder;
    private final String logTable;

    public AttackLogs(final ConnectionHolder connectionHolder,
                      final String logTable) {
        this.connectionHolder = connectionHolder;
        this.logTable = logTable;
    }

    public final Collection<AttackLog> logs(final int duelId) {
        final String query = String.format(""
                + "SELECT attacker_name, target_name, damage "
                + "FROM %s  WHERE duel_id = ? "
                + "ORDER BY time",
                this.logTable
        );
        final Collection<AttackLog> result;
        try (PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setInt(1, duelId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new ArrayList<>();
                    do {
                        result.add(
                                new SimpleAttackLog(
                                        resultSet.getString("attacker_name"),
                                        resultSet.getString("target_name"),
                                        resultSet.getInt("damage")
                        ));
                    } while (resultSet.next());
                } else {
                    result = Collections.emptyList();
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final void create(final String attackerName, final int duelId,
                             final String targetName, final int damage) {
        final String query = String.format(""
                + "INSERT INTO %s (attacker_name, duel_id, target_name, damage) "
                + "VALUES (?, ?, ?, ?)",
                this.logTable
        );
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, attackerName);
            statement.setInt(2, duelId);
            statement.setString(3, targetName);
            statement.setInt(4, damage);
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
