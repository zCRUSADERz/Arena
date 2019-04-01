package ru.job4j.domain;

import java.sql.Timestamp;

public class Duel {
    /**
     * Duel life time in seconds.
     */
    private final long lifeTime;

    public Duel(final Timestamp created, final Timestamp now) {
        this(Math.floorDiv((now.getTime() - created.getTime()), 1000));
    }

    public Duel(final long lifeTime) {
        this.lifeTime = lifeTime;
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
}
