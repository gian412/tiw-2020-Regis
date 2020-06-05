package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/CampaignDetails")
public class GoToCampaignDetails extends HttpServlet {

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
        try {
            cmpId = req.getParameter("campaign"); // Get campaign id from the request
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter value");
            return;
        }
        int campaignId;
        try {
            campaignId = Integer.parseInt(cmpId); // Parse campaign id
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter value");
            return;
        }

        CampaignDAO campaignDAO = new CampaignDAO(connection, campaignId);
        Campaign campaign = null;
        try { // Get the campaign from DB
            campaign = campaignDAO.findCampaignById();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to find campaign");
            return;
        }

        List<Image> images = null;
        try {
            images = campaignDAO.findImagesByCampaign();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to get the images");
            return;
        }

        String path = "/WEB-INF/campaignDetails.html"; // Go to Campaign Details

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        ctx.setVariable("campaign", campaign);
        ctx.setVariable("images", images);
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
