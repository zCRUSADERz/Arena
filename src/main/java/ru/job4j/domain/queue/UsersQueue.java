package ru.job4j.domain.queue;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Users queue.
 *
 * Thread-safe users queue duel.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class UsersQueue {
    private final LinkedBlockingQueue<String> queue;
    /**
     * Collection of internal UserNameHolder in consumer threads.
     * From this class we can check the availability of a user in the
     * userNameHolder and delete it on demand.
     */
    private final ConcurrentLinkedQueue<UserNameHolder> usersInThreads;

    public UsersQueue() {
        this(new LinkedBlockingQueue<>(), new ConcurrentLinkedQueue<>());
    }

    public UsersQueue(
            final LinkedBlockingQueue<String> queue,
            final ConcurrentLinkedQueue<UserNameHolder> usersInThreads) {
        this.queue = queue;
        this.usersInThreads = usersInThreads;
    }

    /**
     * Inserts the user name at the tail of this queue, waiting if
     * necessary up to the specified wait time for space to become available.
     * @param userName user name.
     * @throws InterruptedException if thread interrupted.
     */
    public final void offer(final String userName) throws InterruptedException {
        this.queue.put(userName);
    }

    /**
     * Retrieves and removes the head of this queue, waiting up to the
     * specified wait time if necessary for an element to become available.
     * @return user name from head of this queue.
     * @throws InterruptedException if thread interrupted.
     */
    public final String poll() throws InterruptedException {
        return this.queue.take();
    }

    /**
     * Find user name in this queue and in internal storage consumer threads.
     * @param userName user name.
     * @return true, if found.
     */
    public final boolean find(final String userName) {
        for (String name : this.queue) {
            if (name.equals(userName)) {
                return true;
            }
        }
        for (UserNameHolder holder : this.usersInThreads) {
            if (holder.name().equals(userName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove user from queue.
     * @param userName user name.
     */
    public final void remove(final String userName) {
        this.usersInThreads.forEach(holder -> holder.remove(userName));
        this.queue.remove(userName);
    }

    /**
     * Add consumer userNameHolder in this queue.
     * @param holder consumer user name holder.
     */
    public final void addHolder(final UserNameHolder holder) {
        this.usersInThreads.add(holder);
    }

    /**
     * Remove consumer userNameHolder from this queue.
     * @param holder consumer user name holder.
     */
    public final void removeHolder(final UserNameHolder holder) {
        this.usersInThreads.remove(holder);
    }
}
