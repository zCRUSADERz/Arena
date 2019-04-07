package ru.job4j.domain.duels;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ActiveDuels {
    private final ConnectionHolder connectionHolder;
    private final Duels duels;

    public ActiveDuels(final ConnectionHolder connectionHolder,
                       final Duels duels) {
        this.connectionHolder = connectionHolder;
        this.duels = duels;
    }

    public final Optional<AttackAction> inDuel(final String userName) {
        final Optional<AttackAction> result;
        final String query = ""
                + "SELECT duel_id FROM active_duelists "
                + "WHERE user_name = ? FOR UPDATE";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(new AttackAction(
                            resultSet.getInt("duel_id"),
                            userName,
                            this.duels
                    ));
                } else {
                    result = Optional.empty();
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
