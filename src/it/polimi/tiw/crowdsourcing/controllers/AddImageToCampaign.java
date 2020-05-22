package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.dao.ManagerDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.Resolution;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/AddImageToCampaign")
@MultipartConfig
public class AddImageToCampaign extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public AddImageToCampaign() {
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

        User manager = null;
        HttpSession httpSession = req.getSession();
        manager = (User) httpSession.getAttribute("user");

        String lat = null, lon = null, camp = null, city = null, region = null, provenance = null, dt = null, res = null, path = null;
        Part imagePart = null;
        double latitude, longitude;
        Date date = null;
        int resValue, campaignId, numberOfImages;

        try {
            lat = req.getParameter("latitude");
            lon = req.getParameter("longitude");
            city = req.getParameter("city");
            region = req.getParameter("region");
            provenance = req.getParameter("provenance");
            dt = req.getParameter("date");
            res = req.getParameter("resolution");
            imagePart = req.getPart("image");
            camp = req.getParameter("campaign");
        } catch (NullPointerException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        if (lat==null || lon==null || city==null || region==null || provenance==null || dt==null || res==null || imagePart==null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lon);
            resValue = Integer.parseInt(res);
            campaignId = Integer.parseInt(camp);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad number format");
            return;
        }

        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dt);
        } catch (ParseException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad date format");
            return;
        }

        CampaignDAO campaignDAO = new CampaignDAO(connection, campaignId);

        try {
            numberOfImages = campaignDAO.findNumberOfImageByCampaign();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
            return;
        }
        try {
            InputStream imageStream = imagePart.getInputStream();
            Image image = ImageIO.read(imageStream);
            path = campaignId + "-" + numberOfImages + ".jpg";
            BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            ImageIO.write(bufferedImage, "jpg", new File(path));
        }catch (IOException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to save image");
            return;
        }

        try {
            campaignDAO.addImage(path, latitude, longitude, city, region, provenance, date, Resolution.getResolutionFromInt(resValue));
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to save image in database");
            return;
        }

        ManagerDAO managerDAO = new ManagerDAO(connection, manager.getId());
        Campaign campaign = null;
        List<it.polimi.tiw.crowdsourcing.beans.Image> images;
        try {
            campaign = managerDAO.findCampaignById(campaignId);
            images = campaignDAO.findImagesByCampaign();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
            return;
        }

        path = "/WEB-INF/CampaignDetails.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        ctx.setVariable("campaign", campaign);
        ctx.setVariable("images", images);
        templateEngine.process(path, ctx, resp.getWriter());

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
