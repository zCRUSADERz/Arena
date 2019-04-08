package ru.job4j.domain.queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * User name holder.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class UserNameHolder {
    private final AtomicReference<String> nameHolder;
    /**
     * Not a valid username to use as a null-object in AtomicReference.
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
     * Clears the repository if this name is stored in it.
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
     * Clears the vault. Returns the stored name if the vault was not empty.
     * Returns defaultUser if the repository was empty.
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
