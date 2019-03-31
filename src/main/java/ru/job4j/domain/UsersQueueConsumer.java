package ru.job4j.domain;

public class UsersQueueConsumer implements Runnable {
    private final UserNameHolder nameHolder;
    private final UsersQueue usersQueue;
    private final Duels duels;
    private final String defaultUser;

    public UsersQueueConsumer(final UsersQueue usersQueue, final Duels duels,
                              final String defaultUser) {
        this(new UserNameHolder(defaultUser), usersQueue, duels, defaultUser);
    }

    public UsersQueueConsumer(final UserNameHolder nameHolder,
                              final UsersQueue usersQueue, final Duels duels,
                              final String defaultUser) {
        this.nameHolder = nameHolder;
        this.usersQueue = usersQueue;
        this.duels = duels;
        this.defaultUser = defaultUser;
    }

    @Override
    public final void run() {
        this.usersQueue.addHolder(this.nameHolder);
        try {
            while (!Thread.interrupted()) {
                final String first;
                try {
                    first = this.usersQueue.poll();
                    final String second = this.nameHolder.removeOrDefault(this.defaultUser);
                    if (this.defaultUser.equals(second)) {
                        this.nameHolder.set(first);
                    } else {
                        this.duels.create(first, second);
                    }
                } catch (final InterruptedException e) {
                    final String user = this.nameHolder.removeOrDefault(this.defaultUser);
                    if (!this.defaultUser.equals(user)) {
                        try {
                            this.usersQueue.offer(user);
                        } catch (InterruptedException ex) {
                            throw new IllegalStateException(ex);
                        }
                    }
                    break;
                }
            }
        } finally {
            this.usersQueue.removeHolder(this.nameHolder);
        }
    }
}
