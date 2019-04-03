package ru.job4j.domain.duels.duelists;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDuelistDamage {
    private final String userName;
    private final Connection connection;

    public DBDuelistDamage(String userName, Connection connection) {
        this.userName = userName;
        this.connection = connection;
    }

    public final int damage() {
        final String query = ""
                + "SELECT damage FROM active_duelists "
                + "WHERE user_name = ?";
        final int result;
        try (final PreparedStatement statement = this.connection.prepareStatement(query)) {
            statement.setString(1, this.userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = resultSet.getInt("damage");
                } else {
                    throw new IllegalStateException(String.format(
                            "User: %s not exist in active duels.",
                            this.userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
