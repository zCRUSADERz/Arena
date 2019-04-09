package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.users.auth.UnverifiedUser;
import ru.job4j.domain.users.auth.UsersAuthentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

/**
 * Sign in.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class SignIn extends HttpServlet {
    private UsersAuthentication authentication;
    private Function<HttpServletRequest, UnverifiedUser> userFactory;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/Login.jsp")
                .forward(req, resp);
    }

    @Override
    public final void doPost(final HttpServletRequest req,
                             final HttpServletResponse resp)
            throws ServletException, IOException {
        final UnverifiedUser user = this.userFactory.apply(req);
        final Map<String, String> errors = this.authentication.authorize(user);
        if (errors.isEmpty()) {
            HttpSession session = req.getSession();
            session.setAttribute("userName", user.name());
            resp.sendRedirect(req.getContextPath() + "/arena");
        } else {
            errors.forEach(req::setAttribute);
            this.doGet(req, resp);
        }
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.authentication = DependencyContainer.usersAuthentication();
        this.userFactory = DependencyContainer.usersFactory();
    }
}
