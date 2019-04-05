package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.db.ConnectionHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import java.io.IOException;

public class CloseConnection extends HttpFilter {
    private ConnectionHolder connectionHolder;

    @Override
    public final void doFilter(final ServletRequest req,
                               final ServletResponse res,
                               final FilterChain chain)
            throws IOException, ServletException {
        super.doFilter(req, res, chain);
        try {
            this.connectionHolder.close();
        } catch (final Exception ex) {
            throw new ServletException(ex);
        }
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.connectionHolder = DependencyContainer.connectionHolder();
    }
}
