package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.results.DuelAttackResult;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FinalBlows {
    private final ConnectionHolder connectionHolder;

    public FinalBlows(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public final void create(final DuelAttackResult attackResult) {
        if (!attackResult.killed()) {
            throw new IllegalStateException(String.format(
                    "The user: %s, did not kill, but only wounded %s",
                    attackResult.attacker(), attackResult.target()
            ));
        }
        final String insertQuery = ""
                + "INSERT INTO final_blow (attacker_name, duel_id, target_name) "
                + "VALUE (?, ?, ?)";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(insertQuery)) {
            statement.setString(1, attackResult.attacker());
            statement.setInt(2, attackResult.duelID());
            statement.setString(3, attackResult.target());
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error creating final blow for duel: %s, attacker: %s, target: %s.",
                        attackResult.attacker(), attackResult.duelID(), attackResult.target()
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
