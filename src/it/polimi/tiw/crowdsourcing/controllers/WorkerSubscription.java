package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.AnonymousCampaignDAO;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/WorkerSubscription")
public class WorkerSubscription extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;

    @Override
    public void init() throws ServletException {
        connection = ClientHandler.getConnection(getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User worker = null;
        HttpSession httpSession = req.getSession(); // Get session from the request
        worker = (User) httpSession.getAttribute("user"); // Get worker from the session attribute

        String id = null;
        int campaignId;
        try { // Get campaign ID from the request
            id = req.getParameter("campaign");
        } catch (NullPointerException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter value");
            return;
        }
        try { // Parse campaign ID in order to avoid error
            campaignId = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter value");
            return;
        }

        Campaign campaign = null;
        AnonymousCampaignDAO anonymousCampaignDAO = new AnonymousCampaignDAO(connection);
        try { // Get the campaign from DB
            campaign = anonymousCampaignDAO.findCampaignById(campaignId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database, campaign not found");
            return;
        }

        CampaignDAO campaignDAO = new CampaignDAO(connection, campaign.getId());
        try { // Subscribe the worker to the campaign
            campaignDAO.SubscribeWorkerToCampaign(worker.getId());
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database, subscription failed");
            return;
        }

        resp.sendRedirect("/tiw_2020_Regis/Campaign?campaign=" + campaign.getId());

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
