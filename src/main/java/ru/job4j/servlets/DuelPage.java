package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.ActiveDuels;
import ru.job4j.domain.Duel;
import ru.job4j.domain.Duelist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DuelPage extends HttpServlet {
    private ActiveDuels activeDuels;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final Duel duel = this.activeDuels.duel(userName);
        final Duelist user = duel.duelist(userName);
        req.setAttribute("yourName", user.name());
        req.setAttribute("yourDamage", user.damage());
        req.setAttribute("yourHealth", user.health());
        final Duelist opponent = duel.opponent(userName);
        req.setAttribute("opponentName", opponent.name());
        req.setAttribute("opponentDamage", opponent.damage());
        req.setAttribute("opponentHealth", opponent.health());
        if (duel.started()) {
            if (user.canAttack(opponent)) {
                req.setAttribute("canAttack", true);
            }
        } else {
            req.setAttribute("timer", duel.timer());
        }
        req.getRequestDispatcher("/WEB-INF/views/Duel.jsp")
                .forward(req, resp);
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.activeDuels = DependencyContainer.activeDuels();
    }
}
