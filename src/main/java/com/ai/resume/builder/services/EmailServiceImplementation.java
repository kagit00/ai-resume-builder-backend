package com.ai.resume.builder.services;

import com.ai.resume.builder.exceptions.InternalServerErrorException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

@Service
@AllArgsConstructor
public class EmailServiceImplementation implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public void sendResumeCompletionEmail(String to, String subject, Map<String, Object> templateModel, boolean isFreeUser) {
        String htmlBody = "";
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            Context context = new Context();
            context.setVariables(templateModel);
            if (isFreeUser)
                htmlBody = templateEngine.process("free-user-email-template", context);
            else
                htmlBody = templateEngine.process("premium-user-email-template", context);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}

