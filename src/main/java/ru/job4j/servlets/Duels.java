package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.UsersQueue;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Duels extends HttpServlet {
    private UsersQueue queue;

    @Override
    protected final void doGet(final HttpServletRequest req,
                               final HttpServletResponse resp)
            throws ServletException, IOException {
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/Duels.jsp")
                .forward(req, resp);
    }

    @Override
    protected final void doPost(final HttpServletRequest req,
                                final HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String action = req.getParameter("action");
        if ("start".equals(action)
                && !((boolean) session.getAttribute("waitingFight"))) {
            try {
                this.queue.offer((String) session.getAttribute("userName"));
            } catch (InterruptedException ex) {
                throw new IllegalStateException(ex);
            }
            session.setAttribute("waitingFight", true);
            this.doGet(req, resp);
        } else if ("cancel".equals(action)) {
            this.queue.remove((String) session.getAttribute("userName"));
            session.setAttribute("waitingFight", false);
            this.doGet(req, resp);
        }
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.queue = DependencyContainer.usersQueue();
    }
}
