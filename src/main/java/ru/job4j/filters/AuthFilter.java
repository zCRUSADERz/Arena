package ru.job4j.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * Auth filter.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 2.04.2019
 */
public class AuthFilter extends HttpFilter {

    @Override
    public final void doFilter(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final FilterChain chain)
            throws IOException, ServletException {
        HttpSession session = request.getSession();
        if (Objects.nonNull(session.getAttribute("userName"))) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/");
        }
    }
}
