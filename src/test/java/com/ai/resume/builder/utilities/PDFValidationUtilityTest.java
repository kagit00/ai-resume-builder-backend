package com.ai.resume.builder.utilities;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PDFValidationUtilityTest {

    @Test
    void isValidPdf_validPdfFile_returnsTrue() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/pdf");
        boolean result = PDFValidationUtility.isValidPdf(file);
        assertTrue(result, "The PDF file should be valid.");
    }

    @Test
    void isValidPdf_invalidFileType_returnsFalse() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        boolean result = PDFValidationUtility.isValidPdf(file);
        assertFalse(result, "The file type should not be valid.");
    }

    @Test
    void isValidPdf_nullContentType_returnsFalse() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn(null);
        boolean result = PDFValidationUtility.isValidPdf(file);
        assertFalse(result, "The file content type should not be null.");
    }

    @Test
    void isValidPath_validFile_returnsTrue() throws IOException {
        // Arrange
        File file = mock(File.class);
        Path mockPath = mock(Path.class);
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "appUploads");

        // Mock the behavior of the file's toPath() method
        when(file.toPath()).thenReturn(mockPath);

        // Mock the normalized path
        when(mockPath.normalize()).thenReturn(mockPath);

        // Mock the startsWith method to return true
        when(mockPath.startsWith(tempDir)).thenReturn(true);

        // Mock the writable check
        Files.createDirectories(tempDir); // Ensure directory exists
        assertTrue(Files.isWritable(tempDir)); // Double-check the directory is writable

        // Act
        boolean result = PDFValidationUtility.isValidPath(file);

        // Assert
        assertTrue(result, "The file path should be valid.");
    }


    @Test
    void isValidPath_invalidPath_returnsFalse() {
        // Arrange
        File file = mock(File.class);
        Path mockPath = mock(Path.class);
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "appUploads");

        // Mock the behavior
        when(file.toPath()).thenReturn(mockPath);
        when(mockPath.normalize()).thenReturn(mockPath);
        when(mockPath.startsWith(tempDir)).thenReturn(false);

        // Act
        boolean result = PDFValidationUtility.isValidPath(file);

        // Assert
        assertFalse(result, "The file path should not be valid.");
    }

    @Test
    void isValidPath_noWritePermission_returnsFalse() {
        // Arrange
        File file = mock(File.class);
        Path mockPath = mock(Path.class);
        Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "appUploads");

        // Mock file toPath and normalize behavior
        when(file.toPath()).thenReturn(mockPath);
        when(mockPath.normalize()).thenReturn(mockPath);

        // Mock startsWith behavior
        when(mockPath.startsWith(tempDir)).thenReturn(true);

        // Simulate a non-writable directory
        try (MockedStatic<Files> mockedFiles = Mockito.mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.isWritable(tempDir)).thenReturn(false);

            // Act
            boolean result = PDFValidationUtility.isValidPath(file);

            // Assert
            assertFalse(result, "The file path should be invalid due to lack of write permissions.");
        }
    }


    @Test
    void isValidPath_exceptionThrown_returnsFalse() {
        File file = mock(File.class);
        Path mockPath = mock(Path.class);

        when(file.toPath()).thenThrow(new RuntimeException("Simulated exception"));
        boolean result = PDFValidationUtility.isValidPath(file);
        assertFalse(result, "The file path should be invalid when an exception is thrown.");
    }
}