package com.ai.resume.builder.filters;

import com.ai.resume.builder.encryption.EncryptionUtil;
import com.ai.resume.builder.models.Audit;
import com.ai.resume.builder.repository.AuditRepository;
import com.ai.resume.builder.utilities.DefaultValuesPopulator;
import com.ai.resume.builder.utilities.HeadersUtility;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

/**
 * The type Audit filter.
 */
//@Component
//@Order(2)
public class AuditFilter implements Filter {

    @Value("${app.security.secret-key}")
    private String secretKey;
    private final AuditRepository auditRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuditFilter.class);
    private final EncryptionUtil encryptionUtil;


    public AuditFilter(AuditRepository auditRepository, EncryptionUtil encryptionUtil) {
        this.auditRepository = auditRepository;
        this.encryptionUtil = encryptionUtil;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (ServletException | IOException e) {
            logger.error(e.getMessage());
        } finally {
            saveAuditData(requestWrapper, responseWrapper);
            try {
                responseWrapper.copyBodyToResponse();
                response.flushBuffer();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private void saveAuditData(ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        Audit audit = new Audit();
        try {
            int statusCode = responseWrapper.getStatus();
            String responseHeaders = HeadersUtility.extractResponseHeaders(responseWrapper);
            String responseBody = new String(responseWrapper.getContentAsByteArray());
            String requestBody = requestWrapper.getContentAsString();
            String requestHeaders = HeadersUtility.extractRequestHeaders(requestWrapper);

            audit.setTimestamp(DefaultValuesPopulator.getCurrentTimestamp());
            audit.setMethodName(requestWrapper.getMethod());
            audit.setRequest(encryptionUtil.encrypt(requestHeaders + "\n" + "Request Body: " + requestBody, secretKey));
            audit.setResponse(responseHeaders + "\n" + "Response Body: " + responseBody);
            audit.setStatus(String.valueOf(statusCode));
            audit.setUri(requestWrapper.getRequestURI());
        } catch (Exception e) {
            logger.error("Error saving audit data: {}", e.getMessage());
        }

        auditRepository.save(audit);
    }
}
