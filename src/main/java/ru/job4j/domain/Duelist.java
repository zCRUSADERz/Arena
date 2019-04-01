package ru.job4j.domain;

import java.sql.Timestamp;

public class Duelist {
    private final String name;
    private final int damage;
    private final int health;
    /**
     * Date of last player activity in seconds
     */
    private final long lastActivity;

    public Duelist(final String name, final int damage, final int health,
                   final Timestamp lastActivity) {
        this(
                name, damage, health,
                Math.floorDiv(lastActivity.getTime(), 1000)
        );
    }

    public Duelist(final String name, final int damage, final int health,
                   final long lastActivity) {
        this.name = name;
        this.damage = damage;
        this.health = health;
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

    public final boolean canAttack(final Duelist other) {
        final long time = this.lastActivity - other.lastActivity;
        return time <= 0 || time >= 10;
    }
}
