package ru.job4j.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainPage extends HttpServlet {

    @Override
    protected final void doGet(final HttpServletRequest req,
                               final HttpServletResponse resp)
            throws ServletException, IOException {
        this.getServletContext()
                .getRequestDispatcher("/WEB-INF/views/Arena.jsp")
                .forward(req, resp);
    }
/*
    @Override
    protected final void doPost(final HttpServletRequest req,
                                final HttpServletResponse resp)
            throws ServletException, IOException {
        this.doGet(req, resp);
    }*/
}
