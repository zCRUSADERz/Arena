package ru.job4j.domain;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsersQueue {
    private final DataSource dataSource;

    public UsersQueue(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public final void offer(final String userName) {
        final String select = "INSERT INTO users_queue (user_name) VALUES (?)";
        try (final Connection conn = this.dataSource.getConnection()) {
            try (final PreparedStatement statement = conn.prepareStatement(select)) {
                statement.setString(1, userName);
                statement.executeUpdate();
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
