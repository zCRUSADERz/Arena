package ru.job4j.servlets;

import ru.job4j.DependencyContainer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TechnicalInfo extends HttpServlet {
    private ThreadLocal<Long> requestTimer;
    private ThreadLocal<Integer> queryCounter;
    private ThreadLocal<Long> queryTimer;

    @Override
    public final void service(final HttpServletRequest req,
                              final HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("queryCounter", this.queryCounter.get());
        req.setAttribute("queryTimer", this.queryTimer.get());
        final long start = this.requestTimer.get();
        final long finish = System.currentTimeMillis();
        final long requestTime = finish - start;
        req.setAttribute("requestTimer", requestTime);
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/FooterInfo.jsp")
                .include(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.requestTimer = DependencyContainer.requestTimer();
        this.queryCounter = DependencyContainer.queryCounter();
        this.queryTimer = DependencyContainer.queryTimer();
    }
}
