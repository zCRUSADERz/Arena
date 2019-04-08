package ru.job4j.domain.duels.results;

/**
 * Attack result.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class AttackResult {
    private final String attacker;
    private final String target;
    private final boolean killed;
    private final int damageDone;

    public AttackResult(final String attacker, final String target,
                        final boolean killed, final int damageDone) {
        this.attacker = attacker;
        this.target = target;
        this.killed = killed;
        this.damageDone = damageDone;
    }

    public final String attacker() {
        return this.attacker;
    }

    public final String target() {
        return this.target;
    }

    public final boolean killed() {
        return this.killed;
    }

    public final int damage() {
        return this.damageDone;
    }
}
