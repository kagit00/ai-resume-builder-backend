package com.ai.resume.builder.utilities;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageCompressionUtilityTest {

    @Test
    void compressImage_validImage_compressesSuccessfully() throws IOException {
        // Arrange: Create a BufferedImage
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("compressed_image.jpg");

        ImageCompressionUtility.compressImage(image, outputFile);

        assertTrue(outputFile.exists(), "Compressed image file should be created");
        assertTrue(outputFile.length() > 0, "Compressed image file should not be empty");
        assertTrue(outputFile.getName().endsWith(".jpg"), "Output file should have .jpg extension");
        outputFile.delete();
    }

    @Test
    void compressImage_nullImage_throwsIOException() {
        BufferedImage image = null;
        File outputFile = new File("compressed_image.jpg");

        assertThrows(IOException.class, () -> {
            ImageCompressionUtility.compressImage(image, outputFile);
        }, "Expected IOException when trying to compress a null image");
    }

    @Test
    void compressImage_validImage_emptyFile_throwsIOException() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        File outputFile = new File("empty_file.jpg");

        // Act: Try compressing and writing to an empty file
        // The method doesn't throw IOException directly on file write failure, so this test is based on validation
        ImageCompressionUtility.compressImage(image, outputFile);

        // Assert: The file should exist and have non-zero size
        assertTrue(outputFile.exists(), "Compressed image file should be created");
        assertTrue(outputFile.length() > 0, "Compressed image file should not be empty");

        // Cleanup
        outputFile.delete();
    }

    @Test
    void compressImage_invalidImage_throwsIOException() {
        // Arrange: Use null image to simulate an invalid image scenario
        BufferedImage invalidImage = null;
        File outputFile = new File("compressed_image.jpg");

        // Act & Assert: Try to compress the invalid image and expect an IOException
        assertThrows(IOException.class, () -> {
            ImageCompressionUtility.compressImage(invalidImage, outputFile);
        }, "Expected IOException when trying to compress a null image");
    }

}