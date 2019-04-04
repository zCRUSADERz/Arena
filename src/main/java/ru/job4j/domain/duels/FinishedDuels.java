package ru.job4j.domain.duels;

import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.duels.logs.results.DuelAttackResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FinishedDuels {
    private final Connection connection;
    private final FinalBlows finalBlows;

    public FinishedDuels(final Connection connection,
                         final FinalBlows finalBlows) {
        this.connection = connection;
        this.finalBlows = finalBlows;
    }

    public final void create(final DuelAttackResult attackResult) {
        try {
            final String copyDuel = ""
                    + "INSERT INTO duels_history (duel_id, created) "
                    + "SELECT duel_id, created FROM active_duels WHERE duel_id = ?";
            try (final PreparedStatement statement = this.connection.prepareStatement(copyDuel)) {
                statement.setInt(1, attackResult.duelID());
                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(String.format(
                            "Duel with id: %d, not found.",
                            attackResult.duelID()
                    ));
                }
            }
            final String copyDuelists = ""
                    + "INSERT INTO duelists_history (user_name, duel_id, health, damage) "
                    + "SELECT user_name, duel_id, health, damage "
                    + "FROM active_duelists WHERE duel_id = ?";
            try (final PreparedStatement statement = this.connection.prepareStatement(copyDuelists)) {
                statement.setInt(1, attackResult.duelID());
                if (statement.executeUpdate() != 2) {
                    throw new IllegalStateException(String.format(
                            "In the duel: %d, is not two duelists.",
                            attackResult.duelID()
                    ));
                }
            }
            final String copyLogs = ""
                    + "INSERT INTO attack_log_history "
                    + "         (attacker_name, time, duel_id, target_name, damage) "
                    + "SELECT attacker_name, time, duel_id, target_name, damage "
                    + "FROM attack_log WHERE duel_id = ?";
            try (final PreparedStatement statement = this.connection.prepareStatement(copyLogs)) {
                statement.setInt(1, attackResult.duelID());
                if (statement.executeUpdate() < 1) {
                    throw new IllegalStateException(String.format(
                            "The log for duel: %d, must contain at least one message.",
                            attackResult.duelID()
                    ));
                }
            }
            this.finalBlows.create(attackResult);
            final String deleteActiveDuel = ""
                    + "DELETE FROM active_duels WHERE duel_id = ?";
            try (final PreparedStatement statement = this.connection.prepareStatement(deleteActiveDuel)) {
                statement.setInt(1, attackResult.duelID());
                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(String.format(
                            "Duel: %d, not found.",
                            attackResult.duelID()
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
