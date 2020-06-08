package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.ManagerDAO;
import it.polimi.tiw.crowdsourcing.dao.WorkerDAO;
import it.polimi.tiw.crowdsourcing.utils.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/EditInfos")
@MultipartConfig
public class EditProfile extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public EditProfile() {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // Get user from the session
        User user;
        HttpSession httpSession = req.getSession();
        user = (User) httpSession.getAttribute("user");

        String firstName, lastName, username, email, experienceString, oldPassword, passwordToHash, secondPassword, newPassword, avatar = null;
        Part imagePart;
        int experienceValue = 0;

        // Get first name from the form or from the session
        try {
            firstName = req.getParameter("firstname");
            if (firstName==null || firstName.equals("")){
                firstName = user.getFirstName();
            }
        } catch (NullPointerException e) {
            firstName = user.getFirstName();
        }

        // Get last name from the form or from the session
        try {
            lastName = req.getParameter("lastname");
            if (lastName==null || lastName.equals("")) {
                lastName = user.getLastName();
            }
        } catch (NullPointerException e) {
            lastName = user.getLastName();
        }

        // Get username from the form or from the session
        try {
            username = req.getParameter("username");
            if (username==null || username.equals("")) {
                username = user.getUsername();
            }
        } catch (NullPointerException e) {
            username = user.getUsername();
        }

        // Get email from the form or from the session
        try {
            email = req.getParameter("email");
            if (email==null || email.equals("")) {
                email = user.getEmail();
            } else if (!Email.isValid(email)) {
                String path = "/WEB-INF/editProfile.html";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                ctx.setVariable("emailErrorMessage", "The email entered is not valid");
                templateEngine.process(path, ctx, resp.getWriter());
                return;
            }
        } catch (NullPointerException e) {
            email = user.getEmail();
        }

        if (user.getRole().equals("worker")) {
            // Get experience level from the form or from the session
            try {
                experienceString = req.getParameter("experience");
                if (experienceString==null || experienceString.equals("")) {
                    experienceValue = user.getExperienceLevel().getValue();
                } else {
                    experienceValue = Integer.parseInt(experienceString);

                    if (experienceValue!=0 && experienceValue!=1 && experienceValue!=2){
                        String path = "/WEB-INF/editProfile.html";
                        ServletContext servletContext = getServletContext();
                        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                        ctx.setVariable("experienceErrorMessage", "The experience level entered is not valid");
                        templateEngine.process(path, ctx, resp.getWriter());
                        return;
                    }
                }
            } catch (NullPointerException e) {
                experienceValue = user.getExperienceLevel().getValue();
            } catch (NumberFormatException e) {
                String path = "/WEB-INF/editProfile.html";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                ctx.setVariable("experienceErrorMessage", "The experience level entered is not valid");
                templateEngine.process(path, ctx, resp.getWriter());
                return;
            }

            // Get avatar from the form or from the session
            try {
                imagePart = req.getPart("avatar");
                ImageHandler.deleteImage(getServletContext().getInitParameter("avatar.location") + user.getId() + ".jpeg");
                avatar = user.getId() + ".jpeg";
                File upload = new File(getServletContext().getInitParameter("avatar.location"));
                ImageHandler.saveImage(upload,imagePart, avatar);
            } catch (NullPointerException | ServletException e) {
                avatar = user.getAvatar();
            }
        }

        // Get passwords from the for or from the session
        try {
            oldPassword = req.getParameter("old-password");
            passwordToHash = req.getParameter("password");
            secondPassword = req.getParameter("password-2");
            if (oldPassword!=null && !oldPassword.equals("") && passwordToHash!=null && !passwordToHash.equals("") && secondPassword!=null && !secondPassword.equals("")) {
                boolean result = checkOldPassword(oldPassword, user);
                if (!result) {
                    String path = "/WEB-INF/editProfile.html";
                    ServletContext servletContext = getServletContext();
                    final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                    ctx.setVariable("oldPasswordErrorMessage", "The old password is incorrect");
                    templateEngine.process(path, ctx, resp.getWriter());
                    return;
                }

                if (passwordToHash.equals(secondPassword)) {
                    newPassword = Encryption.hashString(passwordToHash);
                } else {
                    String path = "/WEB-INF/editProfile.html";
                    ServletContext servletContext = getServletContext();
                    final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                    ctx.setVariable("newPasswordErrorMessage", "Password must be equals");
                    templateEngine.process(path, ctx, resp.getWriter());
                    return;
                }
            } else if ( (oldPassword==null || oldPassword.equals("")) && (passwordToHash==null || passwordToHash.equals("")) && (secondPassword==null || secondPassword.equals(""))) {
                newPassword = user.getPassword();
            } else {
                String path = "/WEB-INF/editProfile.html";
                ServletContext servletContext = getServletContext();
                final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
                ctx.setVariable("oldPasswordErrorMessage", "All the passwords fields must be filled to change the password");
                templateEngine.process(path, ctx, resp.getWriter());
                return;
            }

        } catch (NullPointerException e) {
            newPassword = user.getPassword();
        }

        try {
            if (user.getRole().equals("manager")) {
                ManagerDAO managerDAO = new ManagerDAO(connection, user.getId());
                managerDAO.updateManager(firstName, lastName, username, email, newPassword);
            } else {
                WorkerDAO workerDAO = new WorkerDAO(connection, user.getId());
                workerDAO.updateWorker(firstName, lastName, username, newPassword, email, experienceValue, avatar);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to edit the profile");
            return;
        }

        String path = getServletContext().getContextPath() + (user.getRole().equals("manager") ? "/ManagerHome" : "/WorkerHome");
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

    private static boolean checkOldPassword(String passwordToHash, User user) {

        String password = Encryption.hashString(passwordToHash);
        return  user.getPassword().equals(password);

    }
}
