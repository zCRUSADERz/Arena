package ru.job4j.domain.duels;

import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.AttackLog;

import java.util.Collection;

public class Duel extends DuelInfo<Duelist> {
    private final DuelStartCondition startCondition;

    public Duel(final DuelStartCondition startCondition,
                final PairOfDuelist<Duelist> duelists,
                final Collection<AttackLog> logs) {
        super(duelists, logs);
        this.startCondition = startCondition;
    }

    public final boolean started() {
        return this.startCondition.started();
    }

    public final int timer() {
        return this.startCondition.timer();
    }
}
