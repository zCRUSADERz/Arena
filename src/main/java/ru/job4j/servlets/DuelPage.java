package ru.job4j.servlets;

import ru.job4j.DependencyContainer;
import ru.job4j.domain.duels.AttackAction;
import ru.job4j.domain.duels.FinishedDuels;
import ru.job4j.domain.duels.duel.ActiveDuel;
import ru.job4j.domain.duels.duel.DuelAttributes;
import ru.job4j.domain.duels.duel.FinishedDuel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.function.IntFunction;

/**
 * Duel page.
 *
 * The page on which either the active duel
 * or the last duel is displayed is displayed.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 3.04.2019
 */
public class DuelPage extends HttpServlet {
    private IntFunction<ActiveDuel> activeDuelFactory;
    private FinishedDuels finishedDuels;

    /**
     * Render active duel, or the last completed.
     * To work requires an attribute "userName" from the session, and optional
     * request attribute "attackAction". If the attribute "attackAction"
     * is not found, then the last completed duel will be displayed.
     * @param req req.
     * @param resp resp.
     * @throws ServletException ServletException.
     * @throws IOException IOException.
     */
    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        final String userName = (String) session.getAttribute("userName");
        final Optional<AttackAction> optAttack = Optional.ofNullable(
                (AttackAction) req.getAttribute("attackAction")
        );
        final Optional<DuelAttributes> optAttributes;
        if (optAttack.isPresent()) {
            final AttackAction attackAction = optAttack.get();
            final ActiveDuel activeDuel
                    = this.activeDuelFactory.apply(attackAction.duelID());
            optAttributes = Optional.of(activeDuel.attributesFor(userName));
        } else {
            final Optional<FinishedDuel> optionalFinished
                    = this.finishedDuels.last(userName);
            if (optionalFinished.isPresent()) {
                final FinishedDuel finishedDuel = optionalFinished.get();
                optAttributes = Optional.of(
                        finishedDuel.attributesFor(userName)
                );
            } else {
                optAttributes = Optional.empty();
            }
        }
        if (optAttributes.isPresent()) {
            final DuelAttributes attributes = optAttributes.get();
            req.setAttribute("attr", attributes.attributes());
            req.setAttribute("logs", attributes.logAttributes());
            req.getRequestDispatcher("/WEB-INF/views/Duel.jsp")
                    .forward(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/arena/duels");
        }
    }

    /**
     * Post request designed to perform a user attack.
     * To work requires request attribute "attackAction".
     * If the attribute "attackAction" is not found,
     * then user will be redirected to the duel page.
     * @param req req.
     * @param resp resp.
     * @throws IOException IOException.
     */
    @Override
    public final void doPost(final HttpServletRequest req,
                             final HttpServletResponse resp)
            throws IOException {
        final Optional<AttackAction> optAttack = Optional.ofNullable(
                (AttackAction) req.getAttribute("attackAction")
        );
        optAttack.ifPresent(AttackAction::act);
        resp.sendRedirect(req.getContextPath() + "/arena/duel");
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.activeDuelFactory = DependencyContainer.activeDuelFactory();
        this.finishedDuels = DependencyContainer.finishedDuels();
    }
}
