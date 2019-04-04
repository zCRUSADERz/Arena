package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.DBDuel;
import ru.job4j.domain.duels.Duel;
import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.AttackLog;
import ru.job4j.domain.duels.logs.AttackLogs;

import java.sql.Timestamp;
import java.util.Collection;

public class SimpleDuelFactory implements DuelFactory {
    /**
     * Duel start delay in seconds.
     */
    private final int duelStartDelay;
    private final AttackLogs attackLogs;

    public SimpleDuelFactory(final int duelStartDelay,
                             final AttackLogs attackLogs) {
        this.duelStartDelay = duelStartDelay;
        this.attackLogs = attackLogs;
    }

    @Override
    public final Duel duel(final Timestamp created, final Timestamp now,
                           final PairOfDuelist<Duelist> duelists,
                           final Collection<AttackLog> logs) {
        return new Duel(
                new DuelStartCondition(
                        created, now, this.duelStartDelay
                ),
                duelists,
                logs
        );
    }

    @Override
    public final DBDuel duel(final int duelId, final Timestamp created,
                             final Timestamp now,
                             final PairOfDuelist<DBDuelist> duelists) {
        return new DBDuel(
                duelId,
                new DuelStartCondition(
                        created, now, this.duelStartDelay
                ),
                duelists,
                this.attackLogs
        );
    }
}
