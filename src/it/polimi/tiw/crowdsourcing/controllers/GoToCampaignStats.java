package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.CampaignStats;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.dao.CampaignStatsDAO;
import it.polimi.tiw.crowdsourcing.dao.ManagerDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
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
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/CampaignStats")
public class GoToCampaignStats extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String cmpId = null;

        User user = null;
        HttpSession httpSession = req.getSession();
        user = (User) httpSession.getAttribute("user");


        try {
            cmpId = req.getParameter("campaign"); // Get campaign id from the request
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter value");
            return;
        }

        int campaignId;
        try {
            campaignId = Integer.parseInt(cmpId); // Parse campiagn id
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter value");
            return;
        }

        CampaignStatsDAO campaignStatsDAO = new CampaignStatsDAO(connection, campaignId);
        CampaignStats campaignStats = null;
        ManagerDAO managerDAO = new ManagerDAO(connection, user.getId());
        Campaign campaign;


        try {
            campaignStats = campaignStatsDAO.findStatsByCampaignId();
            campaign = managerDAO.findCampaignById(campaignId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "unable to access database");
            return;
        }

        String path = "/WEB-INF/CampaignStats.html";
        final WebContext ctx = new WebContext(req, resp, getServletContext(), req.getLocale());
        ctx.setVariable("campaign", campaign);
        ctx.setVariable("stats", campaignStats);
        templateEngine.process(path, ctx, resp.getWriter());

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
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
