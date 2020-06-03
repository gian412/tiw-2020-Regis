package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.utils.ClientHandler;
import it.polimi.tiw.crowdsourcing.utils.Confidence;
import org.thymeleaf.TemplateEngine;
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

        // TODO: create annotationà

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
