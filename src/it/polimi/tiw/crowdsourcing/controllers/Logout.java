package it.polimi.tiw.crowdsourcing.controllers;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/Logout")
public class Logout extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public Logout() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession httpSession = req.getSession(false); // Get the session only if it already exists
        if (httpSession!=null) { // If the session isn't null...
            httpSession.invalidate(); // ... invalidate it
        }

        String path = null;
        path = getServletContext().getContextPath() + "/welcome.html";
        resp.sendRedirect(path);

    }
}
