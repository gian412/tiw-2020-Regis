package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.ManagerDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CampaignContent")
public class GoToCampaignContent extends HttpServlet {

    private static final long serialVersionUID = 1L;
	private Connection connection;
    TemplateEngine templateEngine;

    public GoToCampaignContent() {
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User manager = null;
        HttpSession httpSession = req.getSession(); // Get session
        manager = (User) httpSession.getAttribute("user"); // Get user from the session

        String id, errorMessage = null;
        int campaignId = 0;

        try {
            id = req.getParameter("id");
            campaignId = Integer.parseInt(id);
        } catch (NullPointerException e1) {
            e1.printStackTrace(); // TODO: remove after test
            errorMessage = "Missing parameter";
        } catch (NumberFormatException e2) {
            e2.printStackTrace(); // TODO: remove after test
            errorMessage = "Invalid parameter value";
        }

        if (errorMessage!=null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, errorMessage);
        }

        ManagerDAO managerDAO = new ManagerDAO(connection, manager.getId());
        Campaign campaign = null;

        try {
            campaign = managerDAO.findCampaignById(campaignId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access the database");
        }

        List<Image> images = null;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);


    }
    // TODO: search images

    @Override
    public void destroy() {
        try {
            ClientHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
        }
    }
}
