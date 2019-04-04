package ru.job4j.domain.duels;

import ru.job4j.domain.duels.logs.AttackResult;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.factories.DuelFactory;
import ru.job4j.domain.duels.factories.DuelistFactory;
import ru.job4j.domain.duels.factories.LogsFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.function.Function;

public class ActiveDuels {
    private final static String DUEL_SELECT = ""
            + "SELECT ad1.user_name, ad1.last_activity, ad1.health, ad1.damage, "
            + "d.duel_id, d.created, CURRENT_TIMESTAMP() AS now, "
            + "ad2.user_name, ad2.last_activity, ad2.health, ad2.damage "
            + "FROM active_duelists AS ad1 "
            + "JOIN active_duels AS d "
            + "ON ad1.duel_id = d.duel_id AND ad1.user_name = ? "
            + "JOIN active_duelists AS ad2 "
            + "ON ad2.duel_id = d.duel_id AND ad2.user_name != ?";
    private final DataSource dataSource;
    private final DuelistFactory duelistFactory;
    private final DuelFactory duelFactory;
    private final LogsFactory logsFactory;
    private final Function<Connection, FinishedDuels> finishedDuelsFactory;

    public ActiveDuels(final DataSource dataSource,
                       final DuelistFactory duelistFactory,
                       final DuelFactory duelFactory,
                       final LogsFactory logsFactory,
                       final Function<Connection, FinishedDuels> finishedDuelsFactory) {
        this.dataSource = dataSource;
        this.duelistFactory = duelistFactory;
        this.duelFactory = duelFactory;
        this.logsFactory = logsFactory;
        this.finishedDuelsFactory = finishedDuelsFactory;
    }

    public final Duel duel(final String userName) {
        final Duel result;
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(DUEL_SELECT)) {
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
                            this.logsFactory.logs(conn, duelID)
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

    public final AttackResult turn(final String userName) {
        final AttackResult result;
        try (final Connection conn = this.dataSource.getConnection()) {
            conn.setAutoCommit(false);
            final int defaultTransactionIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                final DBDuel duel;
                try (final PreparedStatement statement = conn.prepareStatement(DUEL_SELECT)) {
                    statement.setString(1, userName);
                    statement.setString(2, userName);
                    try (final ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            duel = this.duelFactory.duel(
                                    conn,
                                    resultSet.getInt("d.duel_id"),
                                    resultSet.getTimestamp("d.created"),
                                    resultSet.getTimestamp("now"),
                                    new PairOfDuelist<>(
                                            this.duelistFactory.duelist(
                                                    conn,
                                                    resultSet.getString("ad1.user_name"),
                                                    resultSet.getInt("ad1.damage"),
                                                    resultSet.getInt("ad1.health"),
                                                    resultSet.getTimestamp("ad1.last_activity"),
                                                    resultSet.getTimestamp("now")
                                            ),
                                            this.duelistFactory.duelist(
                                                    conn,
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
                }
                result = duel.turn(userName);
                if (result.killed()) {
                    this.finishedDuelsFactory.apply(conn).create(duel.id(), result);
                }
            } catch (final Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setTransactionIsolation(defaultTransactionIsolation);
                conn.setAutoCommit(true);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    public final boolean inDuel(final String userName) {
        final String query = ""
                + "SELECT user_name FROM active_duelists "
                + "WHERE user_name = ?";
        final boolean result;
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                result = resultSet.next();
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
