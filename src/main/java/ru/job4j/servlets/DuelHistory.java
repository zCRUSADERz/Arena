package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.duels.DuelInfo;
import ru.job4j.domain.duels.FinishedDuels;
import ru.job4j.domain.duels.duelists.DuelistInfo;
import ru.job4j.domain.duels.logs.AttackLog;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class DuelHistory extends HttpServlet {
    private FinishedDuels finishedDuels;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final DuelInfo<DuelistInfo> duel = this.finishedDuels.duel(userName);
        final DuelistInfo user = duel.duelist(userName);
        req.setAttribute("user", user);
        final DuelistInfo opponent = duel.opponent(userName);
        req.setAttribute("opponent", opponent);
        final Collection<AttackLog> logs = duel.logs();
        final Collection<String> processedLog = logs
                .stream()
                .sequential()
                .map(attackLog -> attackLog.printFor(userName))
                .collect(Collectors.toList());
        req.setAttribute("logs", processedLog);
        req.getRequestDispatcher("/WEB-INF/views/DuelHistory.jsp")
                .forward(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.finishedDuels = DependencyContainer.finishedDuels();
    }
}
