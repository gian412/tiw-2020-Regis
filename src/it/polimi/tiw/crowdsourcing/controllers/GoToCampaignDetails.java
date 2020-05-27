package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.AnonymousCampaignDAO;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
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

        AnonymousCampaignDAO anonymousCampaignDAO = new AnonymousCampaignDAO(connection);
        Campaign campaign = null;
        try { // Get the campaign from DB
            campaign = anonymousCampaignDAO.findCampaignById(campaignId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
            return;
        }
        CampaignDAO campaignDAO = new CampaignDAO(connection, campaignId);
        List<Image> images = null;
        try {
            images = campaignDAO.findImagesByCampaign();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
            return;
        }


        /*for (Image image : images) {
            String imagePath = "/var/webapps/uploads/images/" + image.getSource(); // Create image path
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(new File(imagePath)); // Try to get the image
            } catch (IOException e) { // If the image isn't available...
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to get the image"); // ... send error
                return;
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", output);
            image.setSource( Base64.getEncoder().encodeToString(output.toByteArray()) );
        }*/



        String path = "/WEB-INF/CampaignDetails.html"; // Go to Campaign Details

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
