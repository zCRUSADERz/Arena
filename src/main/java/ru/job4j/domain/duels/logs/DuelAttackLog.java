package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Attack log.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public class DuelAttackLog {
    private final int duelID;
    private final ConnectionHolder connectionHolder;
    private final String query;

    public DuelAttackLog(final int duelID, final String query,
                         final ConnectionHolder connectionHolder) {
        this.duelID = duelID;
        this.query = query;
        this.connectionHolder = connectionHolder;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * @param userName prepares for user.
     * @return collection of log lines for user.
     */
    public final Collection<String> attributesFor(final String userName) {
        return this.log()
                .stream()
                .sequential()
                .map(attackLog -> attackLog.printFor(userName))
                .collect(Collectors.toList());
    }

    /**
     * @return collection of attack log entries.
     */
    public final Collection<AttackLog> log() {
        final Collection<AttackLog> result;
        try (PreparedStatement statement
                     = this.connectionHolder.connection().prepareStatement(query)) {
            statement.setInt(1, this.duelID);
            try (final ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    result = new ArrayList<>();
                    do {
                        result.add(
                                new AttackLog(
                                        resultSet.getString("attacker_name"),
                                        resultSet.getString("target_name"),
                                        resultSet.getInt("damage")
                                ));
                    } while (resultSet.next());
                } else {
                    result = Collections.emptyList();
                }
            }
        } catch (final SQLException ex) {
            throw new IllegalStateException(ex);
        }
        return result;
    }


}
