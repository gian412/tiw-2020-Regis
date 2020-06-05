package it.polimi.tiw.crowdsourcing.controllers;

import it.polimi.tiw.crowdsourcing.utils.ImageHandler;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/resources/*")
public class GetResources extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public GetResources() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String imageName = null;
        try {
            imageName = req.getPathInfo().substring(1); // Get image name from the request
        } catch (NullPointerException e) { // If the parameter is null...
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameter values"); // ...send error
            return;
        }

        String imagePath = getServletContext().getInitParameter("resources.location") + imageName; // Create image path
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(new File(imagePath)); // Try to get the image
        } catch (IOException e) { // If the image isn't available...
            e.printStackTrace(); // TODO: remove after test
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Unable to get the image"); // ... send error
            return;
        }

        byte[] image = ImageHandler.getImageAsByte(bufferedImage);
        resp.setContentType(getServletContext().getMimeType(imageName));
        resp.setContentLength(image.length);
        resp.getOutputStream().write(image);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
