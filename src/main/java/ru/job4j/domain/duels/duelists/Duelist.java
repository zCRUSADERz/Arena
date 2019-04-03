package ru.job4j.domain.duels.duelists;

import ru.job4j.domain.duels.activity.LastActivity;

public interface Duelist extends SimpleDuelist {

    int damage();

    int health();

    LastActivity activity();

    boolean canAttack(final Duelist other);
}
