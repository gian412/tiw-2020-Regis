package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.UserDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.Encryption;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CreateUser")
public class CreateUser extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    @Override
    public void init() throws ServletException {
        connection = ClientHandler.getConnection(getServletContext());
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver( servletContext );
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role, firstName, lastName, username, password, passwordToHash, email, exp, path;
        try {
            role = req.getParameter("role"); // Get role from request's parameter
            role = role.toLowerCase();
            firstName = req.getParameter("firstname"); // Get first name from request's parameter
            lastName = req.getParameter("lastname"); // Get last name from request's parameter
            username = req.getParameter("username"); // Get username from request's parameter
            email = req.getParameter("email"); // Get email from request's parameter
            passwordToHash = req.getParameter("password"); // Get password from request's parameter

            password = Encryption.hashString(passwordToHash); // Encrypt password
        } catch (NullPointerException e) { // If one of the parameters is null...
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
            return;
        }
        User user = null;
        UserDAO userDAO = new UserDAO(connection);
        try {
            user = userDAO.findUserByEmail(email); // Check if the email already exists
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to check user");
            return;
        }
        if (user!=null) { // If the email already exist
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
            ctx.setVariable("username", user.getUsername());
            ctx.setVariable("errorMessage", "email already associated to an account");
            path = "/Login.html"; // Write path ...
            templateEngine.process(path, ctx, resp.getWriter()); // ...and process it
            return;
        }

        try {
            user = userDAO.findUserByUsername(username); // Check if the username already exists
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to check user");
            return;
        }
        if (user!=null) { // If the username already exists
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
            ctx.setVariable("username", user.getUsername());
            ctx.setVariable("errorMessage", "Username already in use");
            path = "/Login.html"; // Write path ...
            templateEngine.process(path, ctx, resp.getWriter()); // ...and process it
            return;
        }

        if (role.equals("manager")) { // If the user is a manager...
            try {
                userDAO.createManager(firstName, lastName, username, password, email);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to create user");
                return;
            }
            try {
                user = userDAO.findUserByUsername(username);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to get user");
                return;
            }
            req.getSession().setAttribute("user", user);
            path = getServletContext().getContextPath() + "/ManagerHome";
        } else if (role.equals("worker")){
            ExperienceLevel experience;
            try {
                exp = req.getParameter("experience");
            } catch (NullPointerException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
                return;
            }
            experience = ExperienceLevel.getExperienceLevelFromString(exp);
            if (experience==null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter values");
                return;
            }
            try {
                userDAO.createWorker(firstName, lastName, username, password, email, experience.getValue());
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to create user");
                return;
            }
            try {
                user = userDAO.findUserByUsername(username);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to get user");
                return;
            }
            req.getSession().setAttribute("user", user);
            path = getServletContext().getContextPath() + "/WorkerHome";
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid role parameter values"); // ...send error
            return;
        }
        resp.sendRedirect(path);
    }

    @Override
    public void destroy() {
        try {
            ClientHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
        }
    }

}
