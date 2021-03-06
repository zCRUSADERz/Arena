package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Final blow.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class FinalBlow {
    private final int duelID;
    private final ConnectionHolder connectionHolder;

    public FinalBlow(final int duelID, final ConnectionHolder connectionHolder) {
        this.duelID = duelID;
        this.connectionHolder = connectionHolder;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * @param userName prepares for user.
     * @return final blow line for user.
     */
    public final String attributesFor(final String userName) {
        return this.log().printFor(userName);
    }

    /**
     * @return final blow log.
     */
    public final FinalBlowLog log() {
        final FinalBlowLog result;
        final String insertQuery = ""
                + "SELECT attacker_name, target_name "
                + "FROM final_blow WHERE duel_id = ?";
        try (final PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(insertQuery)) {
            statement.setInt(1, this.duelID);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new FinalBlowLog(
                            resultSet.getString("attacker_name"),
                            resultSet.getString("target_name")
                    );
                } else {
                    throw new IllegalStateException(String.format(
                            "Final blow for duel: %d, not found.",
                            duelID
                    ));
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }
}
