package com.ai.resume.builder.services;

import java.util.Map;

public interface EmailService {
    void sendResumeCompletionEmail(String to, String name, String subject, Map<String, Object> templateModel, boolean isFreeUser);
    void sendPremiumSubscriptionEmail(String to, String name);
}
