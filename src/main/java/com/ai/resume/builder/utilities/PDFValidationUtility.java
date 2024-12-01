package com.ai.resume.builder.utilities;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class PDFValidationUtility {

    private PDFValidationUtility() {
        throw new UnsupportedOperationException("Unsupported Operation.");
    }

    public static boolean isValidPdf(MultipartFile file) {
        String contentType = file.getContentType();
        return "application/pdf".equals(contentType);
    }

    public static boolean isValidPath(File file) {
        try {
            Path filePath = file.toPath().normalize();
            Path tempDir = new File(System.getProperty("java.io.tmpdir"), "appUploads").toPath().normalize();

            // Ensure that the file is saved inside the designated temporary directory
            return filePath.startsWith(tempDir) && Files.isWritable(tempDir);
        } catch (Exception e) {
            return false;
        }
    }
}
