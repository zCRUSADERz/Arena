package ru.job4j.domain.rating;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class UserRating {
    private final String userName;
    private final ConnectionHolder connectionHolder;

    public UserRating(final String userName,
                      final ConnectionHolder connectionHolder) {
        this.userName = userName;
        this.connectionHolder = connectionHolder;
    }

    public final Map<String, String> attributes() {
        final Map<String, String> result;
        final String query = ""
                + "SELECT rating, "
                + "(SELECT COUNT(attacker_name) FROM final_blow "
                + "WHERE attacker_name = ?) AS victories, "
                + "(SELECT COUNT(target_name) FROM final_blow  "
                + "WHERE target_name = ?) AS defeat "
                + "FROM users WHERE name = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, userName);
            statement.setString(2, userName);
            statement.setString(3, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Map.of(
                            "rating", String.valueOf(
                                    resultSet.getInt("rating")
                            ),
                            "victories", String.valueOf(
                                    resultSet.getInt("victories")
                            ),
                            "defeat", String.valueOf(
                                    resultSet.getInt("defeat")
                            )
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Rating for user: %s, not found.",
                            userName
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
