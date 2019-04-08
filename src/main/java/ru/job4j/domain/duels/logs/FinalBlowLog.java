package ru.job4j.domain.duels.logs;

/**
 * Final blow log.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 7.04.2019
 */
public class FinalBlowLog {
    private final String attacker;
    private final String target;

    public FinalBlowLog(final String attacker, final String target) {
        this.attacker = attacker;
        this.target = target;
    }

    /**
     * Prepare and print final blow for user.
     * @param userName user name.
     * @return prepared final blow line.
     */
    public final String printFor(final String userName) {
        final String result;
        if (this.attacker.equals(userName)) {
            result = String.format(
                    "Вы убили %s.",
                    this.target
            );
        } else if (this.target.equals(userName)) {
            result = String.format(
                    "%s убил вас.",
                    this.attacker
            );
        } else {
            throw new IllegalStateException(String.format(
                    "User: %s, is not a member of this fight. %s vs %s.",
                    userName, this.attacker, this.target
            ));
        }
        return result;
    }
}
