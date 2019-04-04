package ru.job4j.domain.duels.duelists;

public class DuelistInfo implements SimpleDuelist {
    private final String name;
    private final int damage;
    private final int health;

    public DuelistInfo(final String name, final int damage, final int health) {
        this.name = name;
        this.damage = damage;
        this.health = health;
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
}
