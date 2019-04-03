package ru.job4j.domain.duels;

import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.logs.AttackLog;

import java.util.Collection;

public class Duel {
    private final DuelStartCondition startCondition;
    private final PairOfDuelist<Duelist> duelists;
    private final Collection<AttackLog> logs;

    public Duel(final DuelStartCondition startCondition,
                final PairOfDuelist<Duelist> duelists,
                final Collection<AttackLog> logs) {
        this.startCondition = startCondition;
        this.duelists = duelists;
        this.logs = logs;
    }

    public final boolean started() {
        return this.startCondition.started();
    }

    public final int timer() {
        return this.startCondition.timer();
    }

    public final Duelist duelist(final String userName) {
        return this.duelists.duelist(userName);
    }

    public final Duelist opponent(final String userName) {
        return this.duelists.opponent(userName);
    }

    public final Collection<AttackLog> logs() {
        return this.logs;
    }
}
