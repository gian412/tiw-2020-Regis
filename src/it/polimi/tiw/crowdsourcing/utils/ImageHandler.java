package it.polimi.tiw.crowdsourcing.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

}
