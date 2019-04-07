package ru.job4j.domain.duels;

import ru.job4j.domain.duels.results.DuelAttackResult;

public interface Duels {

    void create(final String first, final String second);

    void finished(final DuelAttackResult attackResult);

    void userTurn(final int duelID, final String userName);
}
