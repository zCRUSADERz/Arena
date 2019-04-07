package ru.job4j.domain.duels;

public class AttackAction {
    private final int duelID;
    private final String userName;
    private final Duels duels;

    public AttackAction(final int duelID, final String userName,
                        final Duels duels) {
        this.duelID = duelID;
        this.userName = userName;
        this.duels = duels;
    }

    public final int duelID() {
        return this.duelID;
    }

    public final void act() {
        this.duels.userTurn(this.duelID, this.userName);
    }
}
