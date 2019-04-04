package ru.job4j.domain.duels.logs;

public class SimpleAttackLog implements AttackLog {
    private final String attackerName;
    private final String targetName;
    private final int damage;

    public SimpleAttackLog(final String attackerName, final String targetName,
                           final int damage) {
        this.attackerName = attackerName;
        this.targetName = targetName;
        this.damage = damage;
    }

    public final String printFor(final String userName) {
        final String result;
        if (this.attackerName.equals(userName)) {
            result = String.format(
                    "Вы ударили %s на %d урона.",
                    this.targetName, this.damage
            );
        } else if (this.targetName.equals(userName)) {
            result = String.format(
                    "%s ударил вас на %d урона.",
                    this.attackerName, this.damage
            );
        } else {
            throw new IllegalStateException(String.format(
                    "User: %s, is not a member of this fight. %s vs %s.",
                    userName, this.attackerName, this.targetName
            ));
        }
        return result;
    }
}
