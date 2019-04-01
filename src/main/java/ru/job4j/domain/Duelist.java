package ru.job4j.domain;

import java.time.LocalDateTime;

public class Duelist {
    private final String name;
    private final int damage;
    private final int health;
    private final LocalDateTime lastActivity;

    public Duelist(final String name, final int damage, final int health,
                   final LocalDateTime lastActivity) {
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

    public final LocalDateTime lastActivity() {
        return this.lastActivity;
    }
}
