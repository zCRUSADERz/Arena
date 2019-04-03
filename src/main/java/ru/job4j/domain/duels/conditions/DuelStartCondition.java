package ru.job4j.domain.duels.conditions;


import java.sql.Timestamp;

public class DuelStartCondition {
    /**
     * Duel life time in seconds.
     */
    private final long lifeTime;
    /**
     * Duel start delay in seconds.
     */
    private final int startDelay;

    public DuelStartCondition(final Timestamp created, final Timestamp now,
                              final int startDelay) {
        this(
                Math.floorDiv((now.getTime() - created.getTime()), 1000),
                startDelay
        );
    }

    public DuelStartCondition(final long lifeTime, final int startDelay) {
        this.lifeTime = lifeTime;
        this.startDelay = startDelay;
    }

    public final boolean started() {
        return this.timer() == 0;
    }

    public final int timer() {
        final long result;
        if (this.lifeTime > this.startDelay) {
            result = 0;
        } else {
            result = this.startDelay - this.lifeTime;
        }
        return (int) result;
    }

    @Override
    public String toString() {
        return String.format(
                "DuelStartCondition{lifeTime=%s (seconds), startDelay=%s (seconds)}",
                this.lifeTime,
                this.startDelay
        );
    }
}
