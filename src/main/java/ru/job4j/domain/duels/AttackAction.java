package ru.job4j.domain.duels;

/**
 * Attack action.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
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

    /**
     * @return duel id this action.
     */
    public final int duelID() {
        return this.duelID;
    }

    /**
     * Perform an attack by this user.
     */
    public final void act() {
        this.duels.userTurn(this.duelID, this.userName);
    }
}
