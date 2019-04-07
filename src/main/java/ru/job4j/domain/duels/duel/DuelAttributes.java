package ru.job4j.domain.duels.duel;

import java.util.Collection;
import java.util.Map;

public class DuelAttributes {
    private final Map<String, String> attr;
    private final Collection<String> logAttr;

    public DuelAttributes(final Map<String, String> attr,
                          final Collection<String> logAttr) {
        this.attr = attr;
        this.logAttr = logAttr;
    }

    public final Map<String, String> attributes() {
        return this.attr;
    }

    public final Collection<String> logAttributes() {
        return this.logAttr;
    }
}
