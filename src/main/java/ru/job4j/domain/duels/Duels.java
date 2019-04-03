package ru.job4j.domain.duels;

import javax.sql.DataSource;
import java.sql.*;

public class Duels {
    private final DataSource dataSource;

    public Duels(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final void create(final String first, final String second)
            throws RuntimeException {
        try (final Connection conn = this.dataSource.getConnection()) {
            conn.setAutoCommit(false);
            final int duel_id;
            try {
                try (final Statement statement = conn.createStatement()) {
                    statement.executeUpdate(
                            "INSERT INTO duels VALUES ()",
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

    private void addDueler(final String userName, final int duelId,
                           final Connection connection) throws SQLException {
        final String query = ""
                + "INSERT INTO users_in_duels (user_name, duel_id, health) "
                + "SELECT name, ? AS duel_id, health FROM users "
                + "WHERE name = ?";
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
