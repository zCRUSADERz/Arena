package ru.job4j.domain.queue;

import java.util.concurrent.atomic.AtomicReference;

public class UserNameHolder {
    private final AtomicReference<String> nameHolder;
    /**
     * Не действительное имя пользователя.
     * Используется вместо null-reference в AtomicReference.
     */
    private final String defaultName;

    public UserNameHolder(final String defaultName) {
        this(new AtomicReference<>(defaultName), defaultName);
    }

    public UserNameHolder(final AtomicReference<String> nameHolder,
                          final String defaultName) {
        this.nameHolder = nameHolder;
        this.defaultName = defaultName;
    }

    /**
     * Очищает хранилище если в нем хранилось это имя.
     * @param userName user name.
     */
    public final void remove(final String userName) {
        this.nameHolder.compareAndExchange(userName, this.defaultName);
    }

    /**
     * Return stored user name.
     * @return user name.
     */
    public final String name() {
        return this.nameHolder.get();
    }

    /**
     * Очищает хранилище. Возвращает хранимое имя, если хранилище не было пусто.
     * Возвращает defaultUser, если хранилище было пусто.
     * @param defaultUser default user name.
     * @return stored user name or default user name.
     */
    public final String removeOrDefault(final String defaultUser) {
        final String storedName =  this.nameHolder.getAndSet(this.defaultName);
        final String result;
        if (storedName.equals(this.defaultName)) {
            result = defaultUser;
        } else {
            result = storedName;
        }
        return result;
    }

    /**
     * Set new user name.
     * @param userName user name.
     */
    public final void set(final String userName) {
        this.nameHolder.set(userName);
    }
}
