package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.UnverifiedUser;
import ru.job4j.domain.UsersAuthentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

public class SignIn extends HttpServlet {
    private UsersAuthentication authentication;

    @Override
    protected void doGet(final HttpServletRequest req,
                         final HttpServletResponse resp)
            throws ServletException, IOException {
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/Login.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req,
                          final HttpServletResponse resp)
            throws ServletException, IOException {
        final UnverifiedUser user = new UnverifiedUser(req);
        final Map<String, String> errors = this.authentication.authorize(user);
        if (errors.isEmpty()) {
            HttpSession session = req.getSession();
            session.setAttribute("userName", user.name());
        } else {
            errors.forEach(req::setAttribute);
            req.getRequestDispatcher("/WEB-INF/views/Login.jsp")
                    .forward(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.authentication = DependencyContainer.usersAuthentication();
    }
}
