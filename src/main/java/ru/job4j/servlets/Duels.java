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
                               final HttpServletResponse resp) throws ServletException, IOException {
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
        if ("start".equals(action)) {
            this.queue.offer((String) session.getAttribute("userName"));
            session.setAttribute("waitingFight", true);
        } else if ("cancel".equals(action)) {
            this.queue.poll((String) session.getAttribute("userName"));
            session.removeAttribute("waitingFight");
            this.doGet(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.queue = DependencyContainer.usersQueue();
    }
}
