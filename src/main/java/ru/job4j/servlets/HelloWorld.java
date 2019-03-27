package ru.job4j.servlets;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HelloWorld extends HttpServlet {

    @Override
    public final void doGet(final HttpServletRequest req,
                            final HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter writer = new PrintWriter(res.getOutputStream());
        writer.append("Hello world");
        writer.flush();
    }
}
