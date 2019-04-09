package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.duels.ActiveDuels;
import ru.job4j.domain.duels.AttackAction;
import ru.job4j.domain.queue.UsersQueue;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * User state filter.
 *
 * The user has three states.
 *
 * First he is not in an active duel and is not looking for an opponent.
 * The second he is in search of an opponent for a duel.
 * Third he is in active duel.
 *
 * These three states are mutually exclusive.
 * Based on these states, requests are routed.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class UserStateFilter extends HttpFilter {
    private ActiveDuels activeDuels;
    private UsersQueue usersQueue;

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.activeDuels = DependencyContainer.activeDuels();
        this.usersQueue = DependencyContainer.usersQueue();
    }

    /**
     * It checks in which of the states the user is now
     * and performs the query routing.
     * @param req req.
     * @param resp resp.
     * @param chain chain.
     * @throws ServletException ServletException.
     * @throws IOException IOException.
     */
    @Override
    public final void doFilter(final HttpServletRequest req,
                               final HttpServletResponse resp,
                               final FilterChain chain)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final Optional<AttackAction> optAttack = this.activeDuels.inDuel(userName);
        if (optAttack.isPresent()) {
            req.setAttribute(
                    "attackAction",
                    optAttack.get()
            );
            req.getRequestDispatcher(req.getContextPath() + "/arena/duel")
                    .forward(req, resp);
        } else if (this.usersQueue.find(userName)) {
            req.setAttribute("waitingFight", true);
            req.getRequestDispatcher(req.getContextPath() + "/arena/duels")
                    .forward(req, resp);
        } else {
            chain.doFilter(req, resp);
        }
    }
}
