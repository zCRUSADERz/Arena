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
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/Duels.jsp")
                .forward(req, resp);
    }

    @Override
    public final void doPost(final HttpServletRequest req,
                             final HttpServletResponse resp)
            throws ServletException, IOException {
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
            req.setAttribute("waitingFight", true);
            this.doGet(req, resp);
        } else if ("cancel".equals(action) && waitingFight) {
            this.queue.remove(userName);
            req.setAttribute("waitingFight", false);
            this.doGet(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.queue = DependencyContainer.usersQueue();
    }
}
