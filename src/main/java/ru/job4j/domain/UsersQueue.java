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
        final String insert = "INSERT INTO users_queue (user_name) VALUES (?)";
        execute(userName, insert);
    }

    public final void poll(final String userName) {
        final String delete =
                "DELETE FROM users_queue WHERE users_queue.user_name = ?";
        execute(userName, delete);
    }

    private void execute(String userName, String query) {
        try (final Connection conn = this.dataSource.getConnection();
             final PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, userName);
            statement.execute();
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
