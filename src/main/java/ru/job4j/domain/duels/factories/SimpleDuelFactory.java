package ru.job4j.domain.duels.factories;

import ru.job4j.domain.duels.DBDuel;
import ru.job4j.domain.duels.Duel;
import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.DBDuelist;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;

import java.sql.Timestamp;

public class SimpleDuelFactory implements DuelFactory {
    /**
     * Duel start delay in seconds.
     */
    private final int duelStartDelay;

    public SimpleDuelFactory(final int duelStartDelay) {
        this.duelStartDelay = duelStartDelay;
    }

    @Override
    public final Duel duel(final Timestamp created, final Timestamp now,
                           final PairOfDuelist<Duelist> duelists) {
        return new Duel(
                new DuelStartCondition(
                        created, now, this.duelStartDelay
                ),
                duelists
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
                duelists
        );
    }
}
