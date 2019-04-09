package ru.job4j.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * Log out.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class LogOut extends HttpServlet {

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws IOException {
        final Optional<HttpSession> optSession
                = Optional.ofNullable(req.getSession(false));
        optSession.ifPresent(HttpSession::invalidate);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
