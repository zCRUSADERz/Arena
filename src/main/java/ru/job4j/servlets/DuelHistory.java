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
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DuelHistory extends HttpServlet {
    private Supplier<FinishedDuels> finishedDuelsFactory;

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        try (final FinishedDuels finishedDuels = this.finishedDuelsFactory.get()) {
            final DuelInfo<DuelistInfo> duel = finishedDuels.duel(userName);
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
        } catch (final Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.finishedDuelsFactory = DependencyContainer.finishedDuels();
    }
}
