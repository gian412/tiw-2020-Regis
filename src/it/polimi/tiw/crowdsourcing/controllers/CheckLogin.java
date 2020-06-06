package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.UserDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.Encryption;
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

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public CheckLogin() {
        super();
    }

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String username;
        String password, passwordToHash;

        try {
            username = req.getParameter("username"); // Get username from request's parameter
            passwordToHash = req.getParameter("password"); // Get the password from request's parameter
            password = Encryption.hashString(passwordToHash);
        } catch (NullPointerException e) { // If one of the parameters is null...
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
            return;
        }
        UserDAO userDAO = new UserDAO(connection);
        User user;
        try {
            user = userDAO.checkCredential(username, password);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to check credential");
            return;
        }
        // If the user exists, save it in the session, check the role and go to the relative home page
        String path;
        if (user!=null) { // If the user exists ...
            req.getSession().setAttribute("user", user); // ...save it in the session, ...
            String target = user.getRole().equals("manager") ? "/ManagerHome" : "/WorkerHome"; // ...check the role ...
            path = getServletContext().getContextPath() + target; // ...and go to the relative homepage, ...
            resp.sendRedirect(path);
        } else { // ...otherwise ...
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
            if (username==null || password==null) { // ...if username or email are null ...
                ctx.setVariable("errorMessage", "Username and password cannot be empty"); // ...write error message ...
            } else { // ...otherwise ...
                ctx.setVariable("errorMessage", "Invalid username or password"); // ...write error message for invalidity ...
            }
            path = "/Login"; // ...write path ...
            templateEngine.process(path, ctx, resp.getWriter()); // ...and process it
        }
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
