package ru.job4j.domain.duels.logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AttackLogs {
    private final Connection connection;

    public AttackLogs(final Connection connection) {
        this.connection = connection;
    }

    public final Collection<AttackLog> logs(final int duelId) {
        final String query = ""
                + "SELECT l.attacker_name, l.target_name, ud.damage "
                + "FROM attack_log AS l JOIN users_in_duels AS ud "
                + "ON l.attacker_name = ud.user_name AND ud.duel_id = ? "
                + "ORDER BY l.time";
        final Collection<AttackLog> result;
        try (PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setInt(1, duelId);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new ArrayList<>();
                    do {
                        result.add(
                                new AttackLog(
                                        resultSet.getString("l.attacker_name"),
                                        resultSet.getString("l.target_name"),
                                        resultSet.getInt("ud.damage")
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
