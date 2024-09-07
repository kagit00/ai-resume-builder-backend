package com.ai.resume.builder.services;

import java.util.Map;

public interface EmailService {
    void sendResumeCompletionEmail(String to, String subject, Map<String, Object> templateModel, boolean isFreeUser);
}
