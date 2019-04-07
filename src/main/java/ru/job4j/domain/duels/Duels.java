package ru.job4j.domain.duels;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.duel.ActiveDuel;
import ru.job4j.domain.duels.logs.FinalBlows;
import ru.job4j.domain.duels.results.DuelAttackResult;
import ru.job4j.domain.users.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.function.Function;
import java.util.function.IntFunction;

public class Duels {
    private final ConnectionHolder connectionHolder;
    private final IntFunction<ActiveDuel> activeDuelFactory;
    private final FinalBlows finalBlows;
    private final Function<String, User> userFactory;

    public Duels(final ConnectionHolder connectionHolder,
                 final IntFunction<ActiveDuel> activeDuelFactory,
                 final FinalBlows finalBlows, Function<String, User> userFactory) {
        this.connectionHolder = connectionHolder;
        this.activeDuelFactory = activeDuelFactory;
        this.finalBlows = finalBlows;
        this.userFactory = userFactory;
    }

    public final void create(final String first, final String second) {
        final int duelID;
        try (final Statement statement
                     = this.connectionHolder.connection().createStatement()) {
            statement.executeUpdate(
                    "INSERT INTO active_duels VALUES ()",
                    Statement.RETURN_GENERATED_KEYS
            );
            try (final ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    duelID = resultSet.getInt(1);
                } else {
                    throw new IllegalStateException(String.format(
                            "Failed to create a new duel for: %s and %s",
                            first, second
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        final ActiveDuel activeDuel = this.activeDuelFactory.apply(duelID);
        activeDuel.addDuelers(first, second);
    }

    public final void userTurn(final int duelID, final String userName) {
        final ActiveDuel activeDuel = this.activeDuelFactory.apply(duelID);
        final DuelAttackResult attackResult = activeDuel.turn(userName);
        if (attackResult.killed()) {
            this.finished(attackResult);
            final User attacker = this.userFactory.apply(attackResult.attacker());
            final User target = this.userFactory.apply(attackResult.target());
            attacker.upgrade(attackResult);
            target.upgrade(attackResult);
        }
    }

    public final void finished(final DuelAttackResult attackResult) {
        try {
            final String copyDuel = ""
                    + "INSERT INTO duels_history (duel_id, created) "
                    + "SELECT duel_id, created FROM active_duels WHERE duel_id = ?";
            try (final PreparedStatement statement
                         = this.connectionHolder.connection().prepareStatement(copyDuel)) {
                statement.setInt(1, attackResult.duelID());
                if (statement.executeUpdate() != 1) {
                    throw new IllegalStateException(String.format(
                            "Duel with id: %d, not found.",
                            attackResult.duelID()
                    ));
                }
            }
            final String copyDuelists = ""
                    + "INSERT INTO duelists_history "
                    + "(user_name, duel_id, start_health, health, damage) "
                    + "SELECT user_name, duel_id, start_health, health, damage "
                    + "FROM active_duelists WHERE duel_id = ?";
            try (final PreparedStatement statement
                         = this.connectionHolder.connection().prepareStatement(copyDuelists)) {
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
            try (final PreparedStatement statement
                         = this.connectionHolder.connection().prepareStatement(copyLogs)) {
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
            try (final PreparedStatement statement
                         = this.connectionHolder.connection().prepareStatement(deleteActiveDuel)) {
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
