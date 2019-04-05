package ru.job4j.domain.rating;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRating {
    private final ConnectionHolder connectionHolder;

    public UsersRating(final ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public final UserRating rating(final String userName) {
        final UserRating result;
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
                    result = new UserRating(
                            resultSet.getInt("rating"),
                            resultSet.getInt("victories"),
                            resultSet.getInt("defeat")
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
