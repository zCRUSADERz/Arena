package ru.job4j.domain.duels.logs.results;

public class SimpleDuelAttackResult
        extends SimpleAttackResult implements DuelAttackResult {
    private final int duelID;

    public SimpleDuelAttackResult(final int duelID,
                                  final AttackResult attackResult) {
        this(
                duelID,
                attackResult.attacker(),
                attackResult.target(),
                attackResult.killed(),
                attackResult.damage()
        );
    }

    public SimpleDuelAttackResult(final int duelID, final String attacker,
                                  final String target, final boolean killed,
                                  final int damageDone) {
        super(attacker, target, killed, damageDone);
        this.duelID = duelID;
    }

    @Override
    public final int duelID() {
        return this.duelID;
    }
}
