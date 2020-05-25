package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.utils.CampaignStatus;
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

@WebServlet("/ChangeCampaignStatus")
public class ChangeCampaignStatus extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    TemplateEngine templateEngine;

    public ChangeCampaignStatus() {
        super();
    }

    @Override
    public void init() throws ServletException {
        connection = ClientHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id, newStat;
        int campaignId, newStatus;

        try {
            id = req.getParameter("id");
            newStat = req.getParameter("status");
        } catch (NullPointerException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter");
            return;
        }

        if (id == null || newStat==null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }

        try {
            campaignId = Integer.parseInt(id);
            newStatus = Integer.parseInt(newStat);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad number format");
            return;
        }

        if (newStatus!=1 && newStatus!=2) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid CampaignStatus code");
            return;
        }

        CampaignDAO campaignDAO = new CampaignDAO(connection, campaignId);
        try {
            campaignDAO.changeStatus(CampaignStatus.getCampaignStatusFromInt(newStatus));
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
            return;
        }

        resp.sendRedirect("/tiw_2020_Regis/CampaignDetails?campaign=" + campaignId);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
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
