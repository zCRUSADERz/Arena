package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.queue.UsersQueue;
import ru.job4j.domain.rating.UserRating;
import ru.job4j.domain.rating.UsersRating;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.function.Supplier;

public class DuelsPage extends HttpServlet {
    private UsersQueue queue;
    private Supplier<UsersRating> usersRatingFactory;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        final boolean waitingFight
                = (Boolean) req.getAttribute("waitingFight");
        if (!waitingFight) {
            HttpSession session = req.getSession();
            final String userName = (String) session.getAttribute("userName");
            try (final UsersRating usersRating = this.usersRatingFactory.get()) {
                final UserRating rating = usersRating.rating(userName);
                req.setAttribute("rating", rating);
            } catch (final Exception ex) {
                throw new ServletException(ex);
            }
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
        final boolean waitingFight
                = (Boolean) req.getAttribute("waitingFight");
        if ("start".equals(action) && !waitingFight) {
            try {
                this.queue.offer(userName);
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
        } else if ("cancel".equals(action) && waitingFight) {
            this.queue.remove(userName);
        }
        resp.sendRedirect(req.getContextPath() + "/arena/duels");
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.queue = DependencyContainer.usersQueue();
        this.usersRatingFactory = DependencyContainer.usersRating();
    }
}
