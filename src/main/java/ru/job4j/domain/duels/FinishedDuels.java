package ru.job4j.domain.duels;

import ru.job4j.db.ConnectionHolder;
import ru.job4j.domain.duels.duel.FinishedDuel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Finished duels.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public class FinishedDuels {
    private final ConnectionHolder connectionHolder;
    private final IntFunction<FinishedDuel> finishedDuelFactory;

    public FinishedDuels(final ConnectionHolder connectionHolder,
                         final IntFunction<FinishedDuel> finishedDuelFactory) {
        this.connectionHolder = connectionHolder;
        this.finishedDuelFactory = finishedDuelFactory;
    }

    /**
     * Finds the last completed duel for the user.
     * @param userName user name.
     * @return optional of last finished duel.
     */
    public final Optional<FinishedDuel> last(final String userName) {
        final Optional<FinishedDuel> result;
        final String query = ""
                + "SELECT dsh.duel_id FROM duelists_history AS dsh "
                + "JOIN duels_history AS dh "
                + "ON dsh.duel_id = dh.duel_id AND dsh.user_name = ? "
                + "ORDER BY dh.finished DESC "
                + "LIMIT 1";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setString(1, userName);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = Optional.of(
                            this.finishedDuelFactory.apply(
                                    resultSet.getInt("dsh.duel_id")
                            )
                    );
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
