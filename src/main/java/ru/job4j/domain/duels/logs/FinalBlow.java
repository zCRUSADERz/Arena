package ru.job4j.domain.duels.logs;

public class FinalBlow implements AttackLog {
    private final String attackerName;
    private final String targetName;

    public FinalBlow(final String attackerName, final String targetName) {
        this.attackerName = attackerName;
        this.targetName = targetName;
    }

    @Override
    public final String printFor(final String userName) {
        final String result;
        if (this.attackerName.equals(userName)) {
            result = String.format(
                    "Вы убили %s.",
                    this.targetName
            );
        } else if (this.targetName.equals(userName)) {
            result = String.format(
                    "%s убил вас.",
                    this.attackerName
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
