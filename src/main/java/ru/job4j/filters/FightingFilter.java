package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.duels.ActiveDuels;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import java.util.function.Supplier;

public class FightingFilter extends HttpFilter {
    private Supplier<ActiveDuels> activeDuelsFactory;

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.activeDuelsFactory = DependencyContainer.activeDuels();
    }

    @Override
    public final void doFilter(final HttpServletRequest req,
                               final HttpServletResponse resp,
                               final FilterChain chain) throws ServletException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final String requestURI = req.getRequestURI();
        try (final ActiveDuels activeDuels = this.activeDuelsFactory.get()) {
            if (activeDuels.inDuel(userName)) {
                session.setAttribute("fighting", true);
                req.setAttribute("fighting", true);
                if (requestURI.equals("/arena/duel")) {
                    chain.doFilter(req, resp);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/arena/duel");
                }
            } else {
                final Optional<Boolean> optFighting = Optional.ofNullable(
                        (Boolean) session.getAttribute("fighting")
                );
                session.setAttribute("fighting", false);
                if (requestURI.equals("/arena/duel")) {
                    if (optFighting.orElse(false)) {
                        resp.sendRedirect(
                                req.getContextPath() + "/arena/duel/history"
                        );
                    } else {
                        resp.sendRedirect(req.getContextPath() + "/arena");
                    }
                } else {
                    chain.doFilter(req, resp);
                }
            }
        } catch (final Exception ex) {
            throw new ServletException(ex);
        }
    }
}
