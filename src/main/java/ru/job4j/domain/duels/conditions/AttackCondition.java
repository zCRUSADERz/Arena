package ru.job4j.domain.duels.conditions;

import ru.job4j.domain.duels.activity.LastActivity;

public interface AttackCondition {

    boolean canAttack(final LastActivity attacker,
                      final LastActivity target);
}
