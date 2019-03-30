package ru.job4j.filters;

import ru.job4j.DependencyContainer;

import javax.servlet.*;
import java.io.IOException;

public class UpdateTechnicalInfo implements Filter {
    private ThreadLocal<Long> requestTimer;
    private ThreadLocal<Integer> queryCounter;
    private ThreadLocal<Long> queryTimer;

    @Override
    public final void init(final FilterConfig filterConfig) {
        this.requestTimer = DependencyContainer.requestTimer();
        this.queryCounter = DependencyContainer.queryCounter();
        this.queryTimer = DependencyContainer.queryTimer();
    }

    @Override
    public final void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain chain)
            throws IOException, ServletException {
        this.requestTimer.set(System.currentTimeMillis());
        this.queryCounter.set(0);
        this.queryTimer.set(0L);
        chain.doFilter(request, response);
    }

    @Override
    public final void destroy() {

    }
}
