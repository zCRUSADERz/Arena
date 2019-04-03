package ru.job4j.domain.duels;

import ru.job4j.domain.duels.conditions.DuelStartCondition;
import ru.job4j.domain.duels.duelists.Duelist;
import ru.job4j.domain.duels.duelists.PairOfDuelist;

public class Duel {
    private final DuelStartCondition startCondition;
    private final PairOfDuelist<Duelist> duelists;

    public Duel(final DuelStartCondition startCondition,
                final PairOfDuelist<Duelist> duelists) {
        this.startCondition = startCondition;
        this.duelists = duelists;
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
}
