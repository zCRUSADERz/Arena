package ru.job4j.domain.duels.duelists;

import ru.job4j.domain.duels.activity.LastActivity;
import ru.job4j.domain.duels.conditions.AttackCondition;

public class ConstantDuelist extends DuelistInfo implements Duelist  {

    private final AttackCondition attackCondition;
    private final LastActivity lastActivity;

    public ConstantDuelist(final String name, final int damage, final int health,
                           final AttackCondition attackCondition,
                           final LastActivity lastActivity) {
        super(name, damage, health);
        this.attackCondition = attackCondition;
        this.lastActivity = lastActivity;
    }

    public final LastActivity activity() {
        return this.lastActivity;
    }

    public final boolean canAttack(final Duelist other) {
        return this.attackCondition
                .canAttack(this.lastActivity, other.activity());
    }
}
