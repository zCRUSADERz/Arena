package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.results.DuelAttackResult;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class ActiveDuelLog {
    private final int duelID;
    private final ConnectionHolder connectionHolder;

    public ActiveDuelLog(final int duelID,
                         final ConnectionHolder connectionHolder) {
        this.duelID = duelID;
        this.connectionHolder = connectionHolder;
    }

    public final Collection<String> attributesFor(final String userName) {
        final String query = ""
                + "SELECT attacker_name, target_name, damage "
                + "FROM attack_log  WHERE duel_id = ? "
                + "ORDER BY time";
        return new DuelLog(
                this.duelID,
                query,
                this.connectionHolder
        ).attributesFor(userName);
    }

    public final void create(final DuelAttackResult attackResult) {
        if (attackResult.duelID() != this.duelID) {
            throw new IllegalStateException(String.format(
                    "DuelID of this attack result does not match the current one. "
                            + "This: %d, attack duelID: %d",
                    this.duelID, attackResult.duelID()
            ));
        }
        final String query = ""
                        + "INSERT INTO attack_log (attacker_name, duel_id, target_name, damage) "
                        + "VALUES (?, ?, ?, ?)";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, attackResult.attacker());
            statement.setInt(2, this.duelID);
            statement.setString(3, attackResult.target());
            statement.setInt(4, attackResult.damage());
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "Error creating a record in the battle log."
                                + "Duel: %s, attacker: %s, target: %s.",
                        this.duelID, attackResult.attacker(), attackResult.damage()
                ));
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
