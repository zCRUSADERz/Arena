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
import java.util.Collection;

public class DuelPage extends HttpServlet {
    private ActiveDuels activeDuels;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final Duel duel = this.activeDuels.duel(userName);
        if (!duel.started()) {
            req.setAttribute("timer", duel.timer());
        }
        final Collection<Duelist> duelists = duel.duelists();
        duelists.forEach(duelist -> {
            final String duelistName = duelist.name();
            if (userName.equals(duelistName)) {
                req.setAttribute("yourName", duelistName);
                req.setAttribute("yourDamage", duelist.damage());
                req.setAttribute("yourHealth", duelist.health());
            } else {
                req.setAttribute("opponentName", duelistName);
                req.setAttribute("opponentDamage", duelist.damage());
                req.setAttribute("opponentHealth", duelist.health());
            }
        });
        req.getRequestDispatcher("/WEB-INF/views/Duel.jsp")
                .forward(req, resp);
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.activeDuels = DependencyContainer.activeDuels();
    }
}
