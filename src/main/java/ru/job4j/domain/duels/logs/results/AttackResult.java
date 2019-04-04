package ru.job4j.domain.duels.logs.results;

public interface AttackResult {

    String attacker();

    String target();

    boolean killed();

    int damage();
}
