package ru.job4j.domain;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class UsersQueue {
    private final LinkedBlockingQueue<String> queue;
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

    public final void offer(final String userName) throws InterruptedException {
        this.queue.put(userName);
    }

    public final String poll() throws InterruptedException {
        return this.queue.take();
    }

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

    public final void remove(final String userName) {
        this.usersInThreads.forEach(holder -> holder.remove(userName));
        this.queue.remove(userName);
    }

    public final void addHolder(final UserNameHolder holder) {
        this.usersInThreads.add(holder);
    }

    public final void removeHolder(final UserNameHolder holder) {
        this.usersInThreads.remove(holder);
    }
}
