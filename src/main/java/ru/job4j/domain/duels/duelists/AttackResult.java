package ru.job4j.domain.duels.duelists;

public class AttackResult {
    private final boolean killed;
    private final int damageDone;

    public AttackResult(final boolean killed, final int damageDone) {
        this.killed = killed;
        this.damageDone = damageDone;
    }

    public final boolean killed() {
        return this.killed;
    }

    public final int damage() {
        return this.damageDone;
    }
}
