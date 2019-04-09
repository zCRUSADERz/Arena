package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.queue.UsersQueue;
import ru.job4j.domain.rating.UserRating;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class DuelsPage extends HttpServlet {
    private UsersQueue queue;
    private Function<String, UserRating> userRatingFactory;
    private int turnDuration;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        final Optional<Boolean> waitingFight = Optional.ofNullable(
                (Boolean) req.getAttribute("waitingFight")
        );
        if (waitingFight.isPresent()) {
            req.setAttribute("turnDuration", this.turnDuration);
        } else {
            HttpSession session = req.getSession();
            final String userName = (String) session.getAttribute("userName");
            req.setAttribute(
                    "ratingAttr",
                    this.userRatingFactory.apply(userName).attributes()
            );
        }
        req.getRequestDispatcher("/WEB-INF/views/Duels.jsp")
                .forward(req, resp);
    }

    @Override
    public final void doPost(final HttpServletRequest req,
                             final HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final String action = req.getParameter("action");
        final Optional<Boolean> waitingFight = Optional.ofNullable(
                (Boolean) req.getAttribute("waitingFight")
        );
        if ("start".equals(action) && waitingFight.isEmpty()) {
            try {
                this.queue.offer(userName);
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        } else if ("cancel".equals(action) && waitingFight.isPresent()) {
            this.queue.remove(userName);
        }
        resp.sendRedirect(req.getContextPath() + "/arena/duels");
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.queue = DependencyContainer.usersQueue();
        this.userRatingFactory = DependencyContainer.usersRating();
        this.turnDuration = DependencyContainer.turnDuration() / 1000;
    }
}
