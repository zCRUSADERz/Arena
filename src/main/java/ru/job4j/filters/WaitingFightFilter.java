package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.UsersQueue;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class WaitingFightFilter implements Filter {
    private UsersQueue usersQueue;

    @Override
    public final void doFilter(final ServletRequest request,
                               final ServletResponse response,
                               final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        if (this.usersQueue.find(userName)) {
            session.setAttribute("waitingFight", true);
            final String requestURI = ((HttpServletRequest) request).getRequestURI();
            if (requestURI.startsWith("/arena/duels")) {
                chain.doFilter(request, response);
            } else {
                request.getRequestDispatcher("/arena/duels")
                        .forward(request, response);
            }
        } else {
            session.setAttribute("waitingFight", false);
            chain.doFilter(request, response);
        }
    }

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.usersQueue = DependencyContainer.usersQueue();
    }

    @Override
    public final void destroy() {

    }
}
