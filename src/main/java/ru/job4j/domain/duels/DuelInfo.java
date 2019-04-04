package ru.job4j.domain.duels;

import ru.job4j.domain.duels.duelists.PairOfDuelist;
import ru.job4j.domain.duels.duelists.SimpleDuelist;
import ru.job4j.domain.duels.logs.AttackLog;

import java.util.Collection;

public class DuelInfo<T extends SimpleDuelist> {
    private final PairOfDuelist<T> duelists;
    private final Collection<AttackLog> logs;

    public DuelInfo(final PairOfDuelist<T> duelists,
                    final Collection<AttackLog> logs) {
        this.duelists = duelists;
        this.logs = logs;
    }

    public final T duelist(final String userName) {
        return this.duelists.duelist(userName);
    }

    public final T opponent(final String userName) {
        return this.duelists.opponent(userName);
    }

    public final Collection<AttackLog> logs() {
        return this.logs;
    }
}
