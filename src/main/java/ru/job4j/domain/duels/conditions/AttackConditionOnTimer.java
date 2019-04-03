package ru.job4j.domain.duels.conditions;

import ru.job4j.domain.duels.activity.LastActivity;

import java.util.function.LongSupplier;

public class AttackConditionOnTimer implements AttackCondition {
    /**
     * Turn duration in milliseconds.
     */
    private final int turnDuration;
    /**
     * The provider of the current time in milliseconds
     * based on the time zone.
     */
    private final LongSupplier currentTimeInMillis;

    public AttackConditionOnTimer(final int turnDuration,
                                  final LongSupplier currentTimeInMillis) {
        this.turnDuration = turnDuration;
        this.currentTimeInMillis = currentTimeInMillis;
    }

    @Override
    public final boolean canAttack(final LastActivity attacker,
                             final LastActivity target) {
        final boolean result;
        final long attackerActivity = attacker.activity();
        final long otherActivity = target.activity();
        if (attackerActivity <= otherActivity) {
            result = true;
        } else {
            final long activity = Math.min(attackerActivity, otherActivity);
            final long time = this.currentTimeInMillis.getAsLong() - activity;
            result = time >= this.turnDuration;
        }
        return result;
    }
}
