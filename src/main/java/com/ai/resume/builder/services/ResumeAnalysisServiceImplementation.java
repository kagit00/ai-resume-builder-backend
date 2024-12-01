package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.BadRequestException;
import com.ai.resume.builder.utilities.PDFValidationUtility;
import org.apache.http.entity.ContentType;
import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.models.ResumeAnalysisResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

@Service
public class ResumeAnalysisServiceImplementation implements ResumeAnalysisService {
    @Value("${ocr.api.key}")
    private String ocrApiKey;
    @Value("${ocr.api.url}")
    private String ocrApiUrl;

    @Override
    public ResumeAnalysisResult analyzeResume(String resumeContent, String jobDescription) {
        if (StringUtils.isEmpty(resumeContent) || StringUtils.isEmpty(jobDescription)) {
            throw new BadRequestException("Resume File Or Job Description is Missing.");
        }

        jobDescription = jobDescription.replaceAll("<[^>]*>", "");
        List<String> jobKeywords = extractKeywordsFromJobDescription(jobDescription);

        List<String> matchedKeywords = new ArrayList<>();
        List<String> missingKeywords = new ArrayList<>();
        resumeContent = resumeContent.toLowerCase();

        for (String keyword : jobKeywords) {
            if (resumeContent.contains(keyword.toLowerCase())) {
                matchedKeywords.add(keyword);
            } else {
                missingKeywords.add(keyword);
            }
        }

        int matchScore = (matchedKeywords.size() * 100) / jobKeywords.size();
        return new ResumeAnalysisResult(matchScore, matchedKeywords, missingKeywords);
    }

    private List<String> extractKeywordsFromJobDescription(String jobDescription) {
        jobDescription = jobDescription.toLowerCase();

        // Simple splitting and filtering of common words (can be improved with NLP libraries)
        String[] commonWords = { "and", "or", "the", "is", "in", "at", "on", "with", "for", "to", "of", "a", "an" };
        Set<String> stopWords = new HashSet<>(Arrays.asList(commonWords));

        String[] words = jobDescription.split("\\W+"); // Split by non-word characters

        List<String> keywords = new ArrayList<>();
        for (String word : words) {
            if (word.length() > 2 && !stopWords.contains(word)) {
                keywords.add(word);
            }
        }
        return keywords.stream().distinct().toList();
    }

    public String extractTextFromPdfImage(MultipartFile file) {
        try {
            File savedPdfFile = savePdfFile(file);

            // upload the PDF directly to the OCR API
            String pdfText = performOcrOnPdf(savedPdfFile);
            if (!StringUtils.isEmpty(pdfText)) {
                deleteTemporaryFile(savedPdfFile);
                return pdfText;
            }

            // If the OCR API can't process PDFs, fallback to converting to images
            List<File> imageFiles = extractImagesFromPdf(savedPdfFile);
            StringBuilder extractedText = new StringBuilder();
            for (File imageFile : imageFiles) {
                String text = performOcrOnImage(imageFile);
                extractedText.append(text).append("\n");
            }
            deleteTemporaryFiles(savedPdfFile, imageFiles);
            return extractedText.toString();
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private File savePdfFile(MultipartFile file) {
        try {
            // Validate the file before saving
            if (!PDFValidationUtility.isValidPdf(file)) {
                throw new BadRequestException("Invalid file type. Only PDF files are allowed.");
            }

            // Create temp directory if it doesn't exist
            File tempDir = new File(System.getProperty("java.io.tmpdir"), "appUploads");
            if (!tempDir.exists()) {
                boolean t = tempDir.mkdirs();
            }

            // Ensure the directory is valid
            if (!PDFValidationUtility.isValidPath(tempDir)) {
                throw new InternalServerErrorException("Invalid temporary directory path.");
            }

            // Save the file to a controlled directory
            File tempFile = new File(tempDir, "uploaded_" + System.currentTimeMillis() + ".pdf");
            file.transferTo(tempFile);
            return tempFile;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }


    // OCR on PDF directly
    private String performOcrOnPdf(File pdfFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(ocrApiUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("apikey", ocrApiKey);
            builder.addBinaryBody("file", pdfFile, ContentType.create("application/pdf"), pdfFile.getName());

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                if (!Objects.isNull(responseEntity)) {
                    String jsonResponse = EntityUtils.toString(responseEntity);
                    return extractTextFromJson(jsonResponse);
                } else {
                    throw new InternalServerErrorException("Empty response from OCR");
                }
            }
        } catch (Exception e) {
            // If PDF is not supported, fallback to image extraction
            return null;
        }
    }

    private List<File> extractImagesFromPdf(File pdfFile) {
        try {
            List<File> imageFiles = new ArrayList<>();
            PDDocument document = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            // Lower DPI to reduce file size (e.g., 150 DPI)
            for (int page = 0; page < document.getNumberOfPages(); page++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 150);  // Lower DPI here
                File imageFile = File.createTempFile("page_" + page, ".jpg");  // Use JPEG for compression
                compressImage(image, imageFile);  // Compress and save image
                imageFiles.add(imageFile);
            }
            document.close();
            return imageFiles;
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    // Compress image before sending it to the OCR API
    private void compressImage(BufferedImage image, File outputFile) throws IOException {
        BufferedImage compressedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        compressedImage.createGraphics().drawImage(image, 0, 0, null);

        // Save compressed image as JPEG with compression quality (75%)
        ImageIO.write(compressedImage, "jpg", outputFile);
    }

    private String performOcrOnImage(File imageFile) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost uploadFile = new HttpPost(ocrApiUrl);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addTextBody("apikey", ocrApiKey);
            builder.addBinaryBody("file", imageFile, org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM, imageFile.getName());

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);

            try (CloseableHttpResponse response = httpClient.execute(uploadFile)) {
                HttpEntity responseEntity = response.getEntity();
                if (!Objects.isNull(responseEntity)) {
                    String jsonResponse = EntityUtils.toString(responseEntity);
                    return extractTextFromJson(jsonResponse);
                } else {
                    throw new InternalServerErrorException("Empty response from OCR API");
                }
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private String extractTextFromJson(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            // Extract the text from the JSON response
            return jsonObject
                    .getJSONArray("ParsedResults")
                    .getJSONObject(0)
                    .getString("ParsedText");
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private void deleteTemporaryFiles(File pdfFile, List<File> imageFiles) {
        try {
            Files.delete(pdfFile.toPath());

            for (File imageFile : imageFiles) {
                Files.delete(imageFile.toPath());
            }
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }

    private void deleteTemporaryFile(File pdfFile) {
        try {
            Files.delete(pdfFile.toPath());
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}


