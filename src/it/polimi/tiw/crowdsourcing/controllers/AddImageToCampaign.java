package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.ImageHandler;
import it.polimi.tiw.crowdsourcing.utils.Resolution;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/AddImageToCampaign")
@MultipartConfig
public class AddImageToCampaign extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    //private TemplateEngine templateEngine;

    public AddImageToCampaign() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ClientHandler.getConnection(getServletContext());
        /*ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver( servletContext );
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");*/
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        String lat, lon, camp, city, region, provenance, dt, res, path;
        Part imagePart;
        double latitude, longitude;
        Date date;
        int resValue, campaignId, numberOfImages;
        Resolution resolution;

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
        } catch (NullPointerException | ServletException e) {
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
            path = campaignId + "-" + numberOfImages + ".jpeg";
            File upload = new File(getServletContext().getInitParameter("image.location"));
            ImageHandler.saveImage(upload, imagePart, path);
        }catch (IOException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to save image");
            return;
        }

        resolution = Resolution.getResolutionFromInt(resValue);
        if (resolution == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid resolution parameter");
            return;
        }

        try {
            campaignDAO.addImage(path, latitude, longitude, city, region, provenance, date, resolution);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to save image in database");
            return;
        }

        path = "/tiw_2020_Regis/CampaignDetails?campaign="+campaignId;
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
