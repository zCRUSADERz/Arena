package ru.job4j.domain.duels.logs;

import ru.job4j.domain.duels.logs.results.DuelAttackResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class FinalBlows implements AutoCloseable {
    private final Supplier<Connection> connectionFactory;

    public FinalBlows(final Supplier<Connection> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public final AttackLog finalBlow(final int duelID) {
        final AttackLog result;
        final String insertQuery = ""
                + "SELECT attacker_name, target_name "
                + "FROM final_blow WHERE duel_id = ?";
        try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(insertQuery)) {
            statement.setInt(1, duelID);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new FinalBlow(
                            resultSet.getString("attacker_name"),
                            resultSet.getString("target_name")
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Final blow for duel: %d, not found.",
                            duelID
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
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
        try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(insertQuery)) {
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

    @Override
    public void close() throws Exception {
        this.connectionFactory.get().close();
    }
}
