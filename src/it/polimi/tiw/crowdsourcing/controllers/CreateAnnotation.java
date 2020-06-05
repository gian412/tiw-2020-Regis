package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.beans.Annotation;
import it.polimi.tiw.crowdsourcing.beans.Campaign;
import it.polimi.tiw.crowdsourcing.beans.Image;
import it.polimi.tiw.crowdsourcing.beans.User;
import it.polimi.tiw.crowdsourcing.dao.AnnotationDAO;
import it.polimi.tiw.crowdsourcing.dao.CampaignDAO;
import it.polimi.tiw.crowdsourcing.dao.ImageDAO;
import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.Confidence;
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

@WebServlet("/InsertAnnotation")
public class CreateAnnotation extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private Connection connection;
    private TemplateEngine templateEngine;

    public CreateAnnotation() {
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

        String imgId, val, conf, note;
        int imageId, validityValue, confidenceValue;
        boolean validity;
        Confidence confidence;

        User worker = null;
        HttpSession httpSession = req.getSession();
        worker = (User) httpSession.getAttribute("user");

        // Get the annotation's parameters from the request
        try {
            imgId = req.getParameter("imageid");
            val = req.getParameter("validity");
            conf = req.getParameter("confidence");
            note = req.getParameter("note");
        } catch (NullPointerException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Form parameters can't be null");
            return;
        }

        // Parse received parameters
        try {
            imageId = Integer.parseInt(imgId);
            validityValue = Integer.parseInt(val);
            confidenceValue = Integer.parseInt(conf);
            if (validityValue==1) {
                validity = true;
            } else if (validityValue==0) {
                validity = false;
            } else {
                throw new NumberFormatException("Invalid Validity value");
            }
            confidence = Confidence.getConfidenceFromInt(confidenceValue);
            if (confidence==null) {
                throw new NumberFormatException("Invalid Confidence value");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameters value");
            return;
        }

        // Check if the annotation already exists
        AnnotationDAO annotationDAO = new AnnotationDAO(connection);
        Annotation annotation;
        try {
            annotation = annotationDAO.findAnnotationByImageAndWorker(imageId, worker.getId());
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access DB");
            return;
        }

        // If the annotation already exists, send error
        if (annotation!=null) {

            // Get the image from the database
            ImageDAO imageDAO = new ImageDAO(connection);
            Image image = null;
            try {
                image = imageDAO.findImageById(imageId);
            } catch (SQLException e) {
                e.printStackTrace(); // TODO: remove after test
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to access database");
                return;
            }
            String path = "/WEB-INF/createAnnotation.html"; // Go to Campaign Details

            ServletContext servletContext = getServletContext();
            final WebContext ctx = new WebContext(req, resp, servletContext, req.getLocale());
            ctx.setVariable("errorMessage", "An annotation for this image already exists");
            ctx.setVariable("image", image);
            templateEngine.process(path, ctx, resp.getWriter());
            return;
        }

        // If the annotation don't exists, create it
        try {
            annotationDAO.createAnnotation(imageId, worker.getId(), validity, confidence, note);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to create the annotation, try again later");
            return;
        }

        // Get the image in order to find campaign ID
        ImageDAO imageDAO = new ImageDAO(connection);
        Image image;
        try {
            image = imageDAO.findImageById(imageId);
        } catch (SQLException e) {
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UNable to access DB");
            return;
        }

        // Send redirect
        String path;
        if (image!=null) {
            path = getServletContext().getContextPath() + "/CampaignOverview?campaign=" + image.getCampaignId();
        } else {
            path = getServletContext().getContextPath() + "/WorkerHome";
        }

        resp.sendRedirect(path);

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
