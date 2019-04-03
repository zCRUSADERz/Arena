package ru.job4j.domain.duels.duelists;

import ru.job4j.domain.duels.activity.LastActivity;
import ru.job4j.domain.duels.conditions.AttackCondition;

public class ConstantDuelist implements Duelist {
    private final String name;
    private final int damage;
    private final int health;
    private final AttackCondition attackCondition;
    private final LastActivity lastActivity;

    public ConstantDuelist(final String name, final int damage, final int health,
                           final AttackCondition attackCondition,
                           final LastActivity lastActivity) {
        this.name = name;
        this.damage = damage;
        this.health = health;
        this.attackCondition = attackCondition;
        this.lastActivity = lastActivity;
    }

    public final String name() {
        return this.name;
    }

    public final int damage() {
        return this.damage;
    }

    public final int health() {
        return this.health;
    }

    public final LastActivity activity() {
        return this.lastActivity;
    }

    public final boolean canAttack(final Duelist other) {
        return this.attackCondition
                .canAttack(this.lastActivity, other.activity());
    }
}
