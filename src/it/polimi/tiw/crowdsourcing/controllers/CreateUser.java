package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.UserDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.ExperienceLevel;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        String role, firstName, lastName, username, password, email, exp = null, avatar;
        try {
            role = req.getParameter("role"); // Get role from request's parameter
            firstName = req.getParameter("firstname"); // Get first name from request's parameter
            lastName = req.getParameter("lastname"); // Get last name from request's parameter
            username = req.getParameter("username"); // Get username from request's parameter
            email = req.getParameter("email"); // Get email from request's parameter
            password = req.getParameter("password"); // Get password from request's parameter
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
            // TODO: forward to login with email field initialized
        }
        String path;
        if (role.equals("manager")) {
            try {
                userDAO.createManager(role, firstName, lastName, username, password, email);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to create user");
                return;
            }
        } else if (role.equals("worker")){
            Part avatarPart = null;
            try {
                exp = req.getParameter("experience");
                avatarPart = req.getPart("avatar");
            } catch (NullPointerException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
                return;
            }
            try {
                int experience = Integer.parseInt(exp);
            } catch (NumberFormatException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Experience must be an integer"); // ...send error
                return;
            }
            if (avatarPart!=null) {
                InputStream imageStream = avatarPart.getInputStream();
                BufferedImage avatarBuff = ImageIO.read(avatarPart.getInputStream());
                try {
                    File outputFile = new File("/WEB-INF/images/avatar/" + username + ".jpg");
                    ImageIO.write(avatarBuff, "jpg", outputFile);
                    avatar = "/WEB-INF/images/avatar/" + username + ".jpg";
                } catch (IOException e) {
                    e.printStackTrace(); // TODO: remove after test
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Not able to save the avatar image");
                    return;
                }
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image parameter value"); // ...send error
                return;
            }

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
