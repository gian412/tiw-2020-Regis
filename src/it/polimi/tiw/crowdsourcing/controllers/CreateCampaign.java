package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.ManagerDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.servlet.RequestDispatcher;
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

@WebServlet("/CreateCampaign")
public class CreateCampaign extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public CreateCampaign() {
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
        HttpSession httpSession = req.getSession(); // Get session from the request
        manager = (User) httpSession.getAttribute("user"); // Get admin from the session attribute
        String campaignName, campaignCustomer;
        try {
            campaignName = req.getParameter("name"); // Get Campaign's name from request's parameter
            campaignCustomer = req.getParameter("customer"); // Get Campaign's customer from request's parameter
        } catch (NullPointerException e) { // If one of the parameters is null ...
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
            return;
        }
        ManagerDAO managerDAO = new ManagerDAO(connection, manager.getId());

        // Try to find a campaign with the same name
        Campaign campaign = null;
        try {
            campaign = managerDAO.findCampaignByName(campaignName);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Unable to find the created campaign");
            return;
        }
        if (campaign!=null){
            List<Campaign> campaigns = null;
            try {
                campaigns = managerDAO.findCampaignsByManager();
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in manager's campaign database extraction");
                return;
            }
            String path = "/WEB-INF/managerHome.html";
            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
            ctx.setVariable("errorMessage", "A campaign with the same name already exists");
            ctx.setVariable("campaignName", campaignName);
            ctx.setVariable("campaignCustomer", campaignCustomer);
            ctx.setVariable("campaigns", campaigns);
            templateEngine.process(path, ctx, resp.getWriter());
            return;
        }

        try {
            managerDAO.createCampaign(campaignName, campaignCustomer);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Campaign creation in the database failed");
            return;
        }

        try {
            campaign = managerDAO.findCampaignByName(campaignName);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Unable to find the created campaign");
            return;
        }

        String path = getServletContext().getContextPath() + "/ManagerHome";
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
