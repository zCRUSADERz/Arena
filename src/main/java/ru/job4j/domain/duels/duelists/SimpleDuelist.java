package ru.job4j.domain.duels.duelists;

import java.util.Map;

public interface SimpleDuelist {

    String name();

    Map<String, String> attributesFor(final String name);
}
