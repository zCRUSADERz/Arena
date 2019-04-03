package ru.job4j.domain.duels;

import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.factories.DuelFactory;
import ru.job4j.domain.duels.factories.DuelistFactory;

import javax.sql.DataSource;
import java.sql.*;

public class ActiveDuels {
    private final static String DUEL_SELECT = ""
            + "SELECT ud1.user_name, ud1.last_activity, ud1.health, us1.damage, "
            + "d.id, d.created, CURRENT_TIMESTAMP() AS now, "
            + "ud2.user_name, ud2.last_activity, ud2.health, us2.damage "
            + "FROM users_in_duels AS ud1 "
            + "JOIN users AS us1 "
            + "ON ud1.user_name = us1.name AND us1.name = ? "
            + "JOIN duels AS d "
            + "ON ud1.duel_id = d.id "
            + "JOIN users_in_duels AS ud2 "
            + "ON ud2.duel_id = d.id AND ud2.user_name != ? "
            + "JOIN users AS us2 "
            + "ON ud2.user_name = us2.name";
    private final DataSource dataSource;
    private final DuelistFactory duelistFactory;
    private final DuelFactory duelFactory;

    public ActiveDuels(final DataSource dataSource,
                       final DuelistFactory duelistFactory,
                       final DuelFactory duelFactory) {
        this.dataSource = dataSource;
        this.duelistFactory = duelistFactory;
        this.duelFactory = duelFactory;
    }

    public final Duel duel(final String userName) {
        final Duel result;
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(DUEL_SELECT)) {
            statement.setString(1, userName);
            statement.setString(2, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = this.duelFactory.duel(
                            resultSet.getTimestamp("d.created"),
                            resultSet.getTimestamp("now"),
                            new PairOfDuelist<>(
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ud1.user_name"),
                                            resultSet.getInt("us1.damage"),
                                            resultSet.getInt("ud1.health"),
                                            resultSet.getTimestamp("ud1.last_activity")
                                    ),
                                    this.duelistFactory.duelist(
                                            resultSet.getString("ud2.user_name"),
                                            resultSet.getInt("us2.damage"),
                                            resultSet.getInt("ud2.health"),
                                            resultSet.getTimestamp("ud2.last_activity")
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
        return result;
    }

    public final void turn(final String userName) {
        final String select = ""
                + "SELECT ud1.user_name, ud1.last_activity, "
                + "d.id, d.created, CURRENT_TIMESTAMP() AS now, "
                + "ud2.user_name, ud2.last_activity "
                + "FROM users_in_duels AS ud1 "
                + "JOIN duels AS d "
                + "ON ud1.duel_id = d.id AND ud1.user_name = ? "
                + "JOIN users_in_duels AS ud2 "
                + "ON ud2.duel_id = d.id AND ud2.user_name != ?";
        try (final Connection conn = this.dataSource.getConnection()) {
            conn.setAutoCommit(false);
            final int defaultTransactionIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                final DBDuel duel;
                try (final PreparedStatement statement = conn.prepareStatement(select)) {
                    statement.setString(1, userName);
                    statement.setString(2, userName);
                    try (final ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            duel = this.duelFactory.duel(
                                    resultSet.getInt("d.id"),
                                    resultSet.getTimestamp("d.created"),
                                    resultSet.getTimestamp("now"),
                                    new PairOfDuelist<>(
                                            this.duelistFactory.duelist(
                                                    conn,
                                                    resultSet.getString("ud1.user_name"),
                                                    resultSet.getTimestamp("ud1.last_activity"),
                                                    resultSet.getTimestamp("now")
                                            ),
                                            this.duelistFactory.duelist(
                                                    conn,
                                                    resultSet.getString("ud2.user_name"),
                                                    resultSet.getTimestamp("ud2.last_activity"),
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
                duel.turn(userName);
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
    }

    public final boolean inDuel(final String userName) {
        final String query = ""
                + "SELECT user_name FROM users_in_duels "
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
