package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Annotation;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.AnnotationDAO;
import it.polimi.tiw.crowdsourcing.dao.ImageDAO;
import it.polimi.tiw.crowdsourcing.dao.UserDAO;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ShowAnnotations")
public class GoToShowAnnotations extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public GoToShowAnnotations() {
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

        String imgId;
        int imageId;
        // Get image ID from the request
        try {
            imgId = req.getParameter("image");
        } catch (NullPointerException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Image parameter can't be null");
            return;
        }

        // Parse parameter to get image ID
        try {
            imageId = Integer.parseInt(imgId);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid image ID");
            return;
        }

        // Get the image from the DB
        ImageDAO imageDAO = new ImageDAO(connection);
        Image image;
        try {
            image = imageDAO.findImageById(imageId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to get the image from the DB");
            return;
        }

        // Get the list of annotation from the DB
        AnnotationDAO annotationDAO = new AnnotationDAO(connection);
        List<Annotation> annotations;
        try {
            annotations = annotationDAO.findAnnotationsByImage(imageId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to get annotations from the DB");
            return;
        }

        // Get the list of worker that made an annotation
        UserDAO userDAO = new UserDAO(connection);
        User worker;
        int workerId;
        for (Annotation annotation : annotations) {
            workerId = annotation.getWorkerId();
            try {
                worker = userDAO.findUserById(workerId);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to get workers from the DB");
                return;
            }
            if (worker!=null) {
                annotation.setWorkerUsername(worker.getUsername());
                annotation.setWorkerAvatar(worker.getAvatar());
            }
        }

        // Go to the annotations' view page
        String path = "/WEB-INF/ShowAnnotations.html";

        ServletContext servletContext = getServletContext();
        final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
        ctx.setVariable("image", image);
        ctx.setVariable("annotations", annotations);
        templateEngine.process(path, ctx, resp.getWriter());
        return;

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
