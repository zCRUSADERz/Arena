package ru.job4j.domain.duels.logs.results;

public class SimpleDuelAttackResult implements DuelAttackResult {
    private final int duelID;
    private final AttackResult origin;

    public SimpleDuelAttackResult(final int duelID, final AttackResult origin) {
        this.duelID = duelID;
        this.origin = origin;
    }

    @Override
    public final int duelID() {
        return this.duelID;
    }

    @Override
    public final String attacker() {
        return this.origin.attacker();
    }

    @Override
    public final String target() {
        return this.origin.target();
    }

    @Override
    public final boolean killed() {
        return this.origin.killed();
    }

    @Override
    public final int damage() {
        return this.origin.damage();
    }
}
