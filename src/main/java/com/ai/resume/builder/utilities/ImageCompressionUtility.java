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
        if (image == null) {
            throw new IOException("Image cannot be null");
        }

        int width = image.getWidth();
        int height = image.getHeight();

        if (width <= 0 || height <= 0) {
            throw new IOException("Invalid image dimensions: width and height must be greater than 0");
        }

        BufferedImage compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        compressedImage.createGraphics().drawImage(image, 0, 0, null);

        // Save compressed image as JPEG with compression quality (75%)
        ImageIO.write(compressedImage, "jpg", outputFile);
    }
}
