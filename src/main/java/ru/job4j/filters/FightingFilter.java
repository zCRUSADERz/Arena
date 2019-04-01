package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.ActiveDuels;

import javax.servlet.*;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class FightingFilter extends HttpFilter {
    private ActiveDuels activeDuels;

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.activeDuels = DependencyContainer.activeDuels();
    }

    @Override
    public final void doFilter(final HttpServletRequest req,
                               final HttpServletResponse resp,
                               final FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final String requestURI = req.getRequestURI();
        if (this.activeDuels.inDuel(userName)) {
            req.setAttribute("fighting", true);
            if (requestURI.equals("/arena/duel")) {
                chain.doFilter(req, resp);
            } else {
                resp.sendRedirect(req.getContextPath() + "/arena/duel");
            }
        } else {
            if (requestURI.equals("/arena/duel")) {
                resp.sendRedirect(req.getContextPath() + "/arena");
            } else {
                chain.doFilter(req, resp);
            }
        }
    }
}
