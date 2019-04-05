package ru.job4j.domain.duels;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.logs.results.DuelAttackResult;
import ru.job4j.domain.users.User;

import java.sql.*;

public class Duels {
    private final ConnectionHolder connectionHolder;
    private final ActiveDuels activeDuels;
    private final FinishedDuels finishedDuels;

    public Duels(final ConnectionHolder connectionHolder,
                 final ActiveDuels activeDuels,
                 final FinishedDuels finishedDuels) {
        this.connectionHolder = connectionHolder;
        this.activeDuels = activeDuels;
        this.finishedDuels = finishedDuels;
    }

    public final void create(final String first, final String second)
            throws RuntimeException {
        try {
            final Connection conn = this.connectionHolder.connection();
            conn.setAutoCommit(false);
            final int duel_id;
            try {
                try (final Statement statement = conn.createStatement()) {
                    statement.executeUpdate(
                            "INSERT INTO active_duels VALUES ()",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    try (final ResultSet resultSet = statement.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            duel_id = resultSet.getInt(1);
                        } else {
                            throw new IllegalStateException(String.format(
                                    "Failed to create a new duel for: %s and %s",
                                    first, second
                            ));
                        }
                    }
                }
                this.addDueler(first, duel_id, conn);
                this.addDueler(second, duel_id, conn);
            } catch (final Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.commit();
                conn.setAutoCommit(true);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public final DuelAttackResult turn(final String userName) {
        final DuelAttackResult result;
        try {
            final Connection conn = this.connectionHolder.connection();
            conn.setAutoCommit(false);
            final int defaultTransactionIsolation = conn.getTransactionIsolation();
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try {
                result = this.activeDuels.turn(userName);
                if (result.killed()) {
                    this.finishedDuels.create(result);
                    new User(result.attacker(), this.connectionHolder)
                            .upgrade(result);
                    new User(result.target(), this.connectionHolder)
                            .upgrade(result);
                }
            } catch (final Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.commit();
                conn.setTransactionIsolation(defaultTransactionIsolation);
                conn.setAutoCommit(true);
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }

    private void addDueler(final String userName, final int duelId,
                           final Connection connection) throws SQLException {
        final String query = ""
                + "INSERT INTO active_duelists "
                + "(user_name, duel_id, start_health, health, damage) "
                + "SELECT name, ? AS duel_id, health AS start_health, health, damage "
                + "FROM users WHERE name = ?";
        try (final PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, duelId);
            statement.setString(2, userName);
            if (statement.executeUpdate() != 1) {
                throw new IllegalStateException(String.format(
                        "failed to attach the user to the duel. User name: %s",
                        userName
                ));
            }
        }
    }
}
