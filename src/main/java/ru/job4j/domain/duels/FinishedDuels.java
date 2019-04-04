package ru.job4j.domain.duels;

import ru.job4j.domain.duels.duelists.DuelistInfo;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.factories.LogsFactory;
import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.duels.logs.results.DuelAttackResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Supplier;

public class FinishedDuels implements AutoCloseable {
    private final Supplier<Connection> connectionFactory;
    private final FinalBlows finalBlows;
    private final LogsFactory logsFactory;

    public FinishedDuels(final Supplier<Connection> connectionFactory,
                         final FinalBlows finalBlows,
                         final LogsFactory logsFactory) {
        this.connectionFactory = connectionFactory;
        this.finalBlows = finalBlows;
        this.logsFactory = logsFactory;
    }


    public final DuelInfo<DuelistInfo> duel(final String userName) {
        final DuelInfo<DuelistInfo> result;
        final String query = ""
                + "SELECT ad1.user_name, ad1.health, ad1.damage, "
                + "d.duel_id, "
                + "ad2.user_name, ad2.health, ad2.damage "
                + "FROM duelists_history AS ad1 "
                + "JOIN duels_history AS d "
                + "ON ad1.duel_id = d.duel_id AND ad1.user_name = ? "
                + "JOIN duelists_history AS ad2  "
                + "ON ad2.duel_id = d.duel_id AND ad2.user_name != ? "
                + "ORDER BY d.finished DESC "
                + "LIMIT 1";
        try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final int duelID = resultSet.getInt("d.duel_id");
                    result = new DuelInfo<>(
                            new PairOfDuelist<>(
                                    new DuelistInfo(
                                            resultSet.getString("ad1.user_name"),
                                            resultSet.getInt("ad1.damage"),
                                            resultSet.getInt("ad1.health")
                                    ),
                                    new DuelistInfo(
                                            resultSet.getString("ad2.user_name"),
                                            resultSet.getInt("ad2.damage"),
                                            resultSet.getInt("ad2.health")
                                    )
                            ),
                            this.logsFactory.logs(duelID)
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Finished duels for user: %s, not found.",
                            userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final void create(final DuelAttackResult attackResult) {
        try {
            final String copyDuel = ""
                    + "INSERT INTO duels_history (duel_id, created) "
                    + "SELECT duel_id, created FROM active_duels WHERE duel_id = ?";
            try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(copyDuel)) {
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
            try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(copyDuelists)) {
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
            try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(copyLogs)) {
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
            try (final PreparedStatement statement = this.connectionFactory.get().prepareStatement(deleteActiveDuel)) {
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

    @Override
    public void close() throws Exception {
        this.connectionFactory.get().close();
    }
}
