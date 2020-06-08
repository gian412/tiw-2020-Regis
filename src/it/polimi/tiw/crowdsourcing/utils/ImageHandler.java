package it.polimi.tiw.crowdsourcing.utils;

import javax.imageio.ImageIO;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class ImageHandler {

    private ImageHandler() {
    }

    public static byte[] getImageAsByte(BufferedImage bufferedImage) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", output);
        output.flush();
        byte[] imageAsByte = output.toByteArray();
        output.close();
        return imageAsByte;

    }

    public static void saveImage(File upload, Part part, String path) throws IOException{

        File image = new File(upload, path);
        try (InputStream input = part.getInputStream()) {
            Files.copy(input, image.toPath());
        }

    }

    public static void deleteImage(String path) {

        try {
            File image = new File(path);
            boolean result = image.delete();
        } catch (Exception e) {
            return;
        }
        return;
    }

}
