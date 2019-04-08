package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Finished duel log.
 *
 * Log for completed duels. Includes a log of all attacks and a final blow log.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 6.04.2019
 */
public class FinishedDuelLog {
    private final int duelID;
    private final ConnectionHolder connectionHolder;
    private final FinalBlow finalBlow;

    public FinishedDuelLog(final int duelID,
                           final ConnectionHolder connectionHolder,
                           final FinalBlow finalBlow) {
        this.duelID = duelID;
        this.connectionHolder = connectionHolder;
        this.finalBlow = finalBlow;
    }

    /**
     * Prepares all the necessary information for rendering the page.
     * @param userName prepares for user.
     * @return collection of log lines for user.
     */
    public final Collection<String> attributesFor(final String userName) {
        final String query = ""
                + "SELECT attacker_name, target_name, damage "
                + "FROM attack_log_history  WHERE duel_id = ? "
                + "ORDER BY time";
        final List<String> result = new ArrayList<>(
                new DuelAttackLog(
                        this.duelID,
                        query,
                        this.connectionHolder
                ).attributesFor(userName)
        );
        result.add(this.finalBlow.attributesFor(userName));
        return result;
    }
}
