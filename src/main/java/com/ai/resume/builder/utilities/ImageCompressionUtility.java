package com.ai.resume.builder.utilities;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageCompressionUtility {

    private ImageCompressionUtility() {
        throw new UnsupportedOperationException("Unsupported Operation");
    }

    public static void compressImage(BufferedImage image, File outputFile) throws IOException {
        BufferedImage compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        compressedImage.createGraphics().drawImage(image, 0, 0, null);

        // Save compressed image as JPEG with compression quality (75%)
        ImageIO.write(compressedImage, "jpg", outputFile);
    }
}
