package com.ai.resume.builder.controllers;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import com.ai.resume.builder.services.EmailService;
import com.ai.resume.builder.services.EmailServiceImplementation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resume/completed")
public class EmailController {
    @Value("${ui.domain.uri}")
    private String uiDomainUri;
    private final EmailServiceImplementation emailService;

    public EmailController(EmailServiceImplementation emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestParam String username, @RequestParam boolean isFreeUser) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("username", username);
        templateModel.put("resumeLink", uiDomainUri);

        try {
            emailService.sendResumeCompletionEmail(username, "Your Resume is Ready", templateModel, isFreeUser);
            return new ResponseEntity<>("Email sent successfully!", HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
