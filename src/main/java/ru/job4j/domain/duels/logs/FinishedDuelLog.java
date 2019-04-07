package ru.job4j.domain.duels.logs;

import ru.job4j.db.ConnectionHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    public final Collection<String> attributesFor(final String userName) {
        final String query = ""
                + "SELECT attacker_name, target_name, damage "
                + "FROM attack_log_history  WHERE duel_id = ? "
                + "ORDER BY time";
        final List<String> result = new ArrayList<>(
                new DuelLog(
                        this.duelID,
                        query,
                        this.connectionHolder
                ).attributesFor(userName)
        );
        result.add(this.finalBlow.attributesFor(userName));
        return result;
    }
}
