package ru.job4j.domain.duels;

import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.factories.DuelFactory;
import ru.job4j.domain.duels.factories.DuelistFactory;
import ru.job4j.domain.duels.factories.LogsFactory;
import ru.job4j.domain.duels.logs.results.DuelAttackResult;
import ru.job4j.domain.duels.logs.results.SimpleDuelAttackResult;

import java.sql.*;
import java.util.function.Supplier;

public class ActiveDuels implements AutoCloseable {
    private final static String DUEL_SELECT = ""
            + "SELECT ad1.user_name, ad1.last_activity, ad1.health, ad1.damage, "
            + "d.duel_id, d.created, CURRENT_TIMESTAMP() AS now, "
            + "ad2.user_name, ad2.last_activity, ad2.health, ad2.damage "
            + "FROM active_duelists AS ad1 "
            + "JOIN active_duels AS d "
            + "ON ad1.duel_id = d.duel_id AND ad1.user_name = ? "
            + "JOIN active_duelists AS ad2 "
            + "ON ad2.duel_id = d.duel_id AND ad2.user_name != ?";
    private final Supplier<Connection> connectionFactory;
    private final DuelistFactory duelistFactory;
    private final DuelFactory duelFactory;
    private final LogsFactory logsFactory;

    public ActiveDuels(final Supplier<Connection> connectionFactory,
                       final DuelistFactory duelistFactory,
                       final DuelFactory duelFactory,
                       final LogsFactory logsFactory) {
        this.connectionFactory = connectionFactory;
        this.duelistFactory = duelistFactory;
        this.duelFactory = duelFactory;
        this.logsFactory = logsFactory;
    }


    public final Duel duel(final String userName) {
        final Duel result;
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(DUEL_SELECT)) {
            statement.setString(1, userName);
            statement.setString(2, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    final int duelID = resultSet.getInt("d.duel_id");
                    result = this.duelFactory.duel(
                            resultSet.getTimestamp("d.created"),
                            resultSet.getTimestamp("now"),
                            new PairOfDuelist<>(
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ad1.user_name"),
                                            resultSet.getInt("ad1.damage"),
                                            resultSet.getInt("ad1.health"),
                                            resultSet.getTimestamp("ad1.last_activity")
                                    ),
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ad2.user_name"),
                                            resultSet.getInt("ad2.damage"),
                                            resultSet.getInt("ad2.health"),
                                            resultSet.getTimestamp("ad2.last_activity")
                                    )
                            ),
                            this.logsFactory.logs(duelID)
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Duel for user: %s, not found.",
                            userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final DuelAttackResult turn(final String userName) {
        final DBDuel duel;
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(DUEL_SELECT)) {
            statement.setString(1, userName);
            statement.setString(2, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    duel = this.duelFactory.duel(
                            resultSet.getInt("d.duel_id"),
                            resultSet.getTimestamp("d.created"),
                            resultSet.getTimestamp("now"),
                            new PairOfDuelist<>(
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ad1.user_name"),
                                            resultSet.getInt("ad1.damage"),
                                            resultSet.getInt("ad1.health"),
                                            resultSet.getTimestamp("ad1.last_activity"),
                                            resultSet.getTimestamp("now")
                                    ),
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ad2.user_name"),
                                            resultSet.getInt("ad2.damage"),
                                            resultSet.getInt("ad2.health"),
                                            resultSet.getTimestamp("ad2.last_activity"),
                                            resultSet.getTimestamp("now")
                                    )
                            )
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Duel for user: %s, not found.",
                            userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return new SimpleDuelAttackResult(duel.id(), duel.turn(userName));
    }

    public final boolean inDuel(final String userName) {
        final String query = ""
                + "SELECT user_name FROM active_duelists "
                + "WHERE user_name = ?";
        final boolean result;
        try (final PreparedStatement statement
                     = this.connectionFactory.get().prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next();
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    @Override
    public void close() throws Exception {
        this.connectionFactory.get().close();
    }
}
