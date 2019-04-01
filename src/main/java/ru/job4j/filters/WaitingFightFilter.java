package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.UsersQueue;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

public class WaitingFightFilter extends HttpFilter {
    private UsersQueue usersQueue;

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.usersQueue = DependencyContainer.usersQueue();
    }

    @Override
    public final void doFilter(final HttpServletRequest req,
                               final HttpServletResponse res,
                               final FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final String requestURI = req.getRequestURI();
        Optional<Object> optFighting = Optional.ofNullable(
                req.getAttribute("fighting")
        );
        if (optFighting.isEmpty() && this.usersQueue.find(userName)) {
            if (requestURI.equals("/arena/duels")) {
                req.setAttribute("waitingFight", true);
                chain.doFilter(req, res);
            } else {
                res.sendRedirect(req.getContextPath() + "/arena/duels");
            }
        } else {
            req.setAttribute("waitingFight", false);
            chain.doFilter(req, res);
        }
    }
}
