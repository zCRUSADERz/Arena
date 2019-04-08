package ru.job4j.domain.duels.results;

/**
 * Duel attack result.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 4.04.2019
 */
public class DuelAttackResult
        extends AttackResult {
    private final int duelID;

    public DuelAttackResult(final int duelID,
                            final AttackResult attackResult) {
        this(
                duelID,
                attackResult.attacker(),
                attackResult.target(),
                attackResult.killed(),
                attackResult.damage()
        );
    }

    public DuelAttackResult(final int duelID, final String attacker,
                            final String target, final boolean killed,
                            final int damageDone) {
        super(attacker, target, killed, damageDone);
        this.duelID = duelID;
    }

    public final int duelID() {
        return this.duelID;
    }
}
