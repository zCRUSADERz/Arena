package ru.job4j.filters;

import ru.job4j.DependencyContainer;
import ru.job4j.db.ConnectionHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import java.util.Optional;

/**
 * Close connection.
 *
 * @author Alexander Yakovlev (sanyakovlev@yandex.ru)
 * @since 5.04.2019
 */
public class CloseConnection extends HttpFilter {
    private ConnectionHolder connectionHolder;

    /**
     * Commit, setAutoCommit(true) and closes the connection if it was open.
     * If chain.doFilter throws some exception, it will be rolled back.
     * @param req request.
     * @param res response.
     * @param chain chain.
     * @throws ServletException ServletException.
     */
    @Override
    public final void doFilter(final ServletRequest req,
                               final ServletResponse res,
                               final FilterChain chain) throws ServletException {
        try (final ConnectionHolder holder = this.connectionHolder) {
            Optional<Exception> optException = Optional.empty();
            try {
                chain.doFilter(req, res);
                this.connectionHolder.commit();
            } catch (final Exception ex) {
                optException = Optional.of(ex);
            } finally {
                try {
                    holder.connection().setAutoCommit(true);
                } catch (final Exception ex) {
                    if (optException.isPresent()) {
                        optException.get().addSuppressed(ex);
                    } else {
                        optException = Optional.of(ex);
                    }
                }
                if (optException.isPresent()) {
                    //noinspection ThrowFromFinallyBlock
                    throw optException.get();
                }
            }
        } catch (final Exception ex) {
            try {
                this.connectionHolder.rollback();
            } catch (final Exception ex2) {
                ex.addSuppressed(ex);
            }
            throw new ServletException(ex);
        }
    }

    @Override
    public final void init() throws ServletException {
        super.init();
        this.connectionHolder = DependencyContainer.connectionHolder();
    }
}
