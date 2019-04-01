package ru.job4j.domain;

import java.sql.Timestamp;
import java.util.Collection;

public class Duel {
    /**
     * Duel life time in seconds.
     */
    private final long lifeTime;
    private final Collection<Duelist> duelists;

    public Duel(final Timestamp created, final Timestamp now,
                final Collection<Duelist> duelists) {
        this(
                Math.floorDiv((now.getTime() - created.getTime()), 1000),
                duelists
        );
    }

    public Duel(final long lifeTime, final Collection<Duelist> duelists) {
        this.lifeTime = lifeTime;
        this.duelists = duelists;
    }

    public final boolean started() {
        return this.timer() == 0;
    }

    public final int timer() {
        final long result;
        if (this.lifeTime > 30) {
            result = 0;
        } else {
            result = 30 - this.lifeTime;
        }
        return (int) result;
    }

    public final Collection<Duelist> duelists() {
        return this.duelists;
    }
}
