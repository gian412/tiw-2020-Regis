package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.WorkerDAO;
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
import java.util.List;

@WebServlet("/WorkerHome")
public class GoToWorkerHome extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public GoToWorkerHome() {
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

        User user = null;
        HttpSession httpSession = req.getSession();
        user = (User) httpSession.getAttribute("user");
        WorkerDAO workerDAO = new WorkerDAO(connection, user.getId());
        List<Campaign> campaignsNotAssigned = null;
        List<Campaign> campaignsAssigned = null;
        try {
            campaignsNotAssigned = workerDAO.findCampaignByWorkerNotAssigned();
            campaignsAssigned = workerDAO.findCampaignByWorkerAssigned();
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY, "Failure in worker's campaign database extraction");
            return;
        }
        String path = "/WEB-INF/workerHome.html";
        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        ctx.setVariable("campaignsNotAssigned", campaignsNotAssigned);
        ctx.setVariable("campaignsAssigned", campaignsAssigned);
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
