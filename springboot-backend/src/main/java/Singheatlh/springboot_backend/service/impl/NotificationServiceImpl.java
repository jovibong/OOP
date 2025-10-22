package Singheatlh.springboot_backend.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import Singheatlh.springboot_backend.dto.EmailRequest;
import Singheatlh.springboot_backend.dto.EmailResponse;
import Singheatlh.springboot_backend.entity.QueueTicket;
import Singheatlh.springboot_backend.service.NotificationService;

/**
 * Implementation of NotificationService
 * Integrated with SMU Lab Notification Service for sending Email notifications
 * Currently uses hardcoded email address for testing
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    // Hardcoded email address for testing
    private static final String HARDCODED_EMAIL_ADDRESS = "ashy.chung@gmail.com";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${smu.notification.api.base-url}")
    private String apiBaseUrl;
    
    @Value("${smu.notification.api.send-email-endpoint}")
    private String sendEmailEndpoint;
    
    @Override
    public void sendQueueNotification3Away(QueueTicket queueTicket) {
        String subject = "Queue Update - 3 Patients Away";
        String message = String.format(
            "Dear Patient,\n\n" +
            "Queue #%d: You are currently 3 patients away from being called. " +
            "Please proceed closer to the consultation room.\n\n" +
            "Thank you for your patience.",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📧 EMAIL NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), subject);
        
        // Send Email via SMU Lab Notification Service
        sendEmail(queueTicket.getPatientId(), subject, message);
    }
    
    @Override
    public void sendQueueNotificationNext(QueueTicket queueTicket) {
        String subject = "Queue Update - You're Next!";
        String message = String.format(
            "Dear Patient,\n\n" +
            "Queue Number #%d: You are next in line. " +
            "Please be ready and stay close to the consultation room.\n\n" +
            "Thank you for your patience.",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📧 EMAIL NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), subject);
        
        // Send Email via SMU Lab Notification Service
        sendEmail(queueTicket.getPatientId(), subject, message);
    }
    
    @Override
    public void sendQueueCalledNotification(QueueTicket queueTicket) {
        String subject = "Queue Called - Your Turn Now!";
        String message = String.format(
            "Dear Patient,\n\n" +
            "Queue #%d: It's your turn now. " +
            "Please proceed to the consultation room immediately.\n\n" +
            "Thank you for your cooperation.",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📧 EMAIL NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), subject);
        
        // Send Email via SMU Lab Notification Service
        sendEmail(queueTicket.getPatientId(), subject, message);
    }
    
    /**
     * Send Email using SMU Lab Notification Service API
     * Currently uses hardcoded email address for testing
     * @param patientId Patient ID (for logging purposes)
     * @param subject Email subject line
     * @param message Email message content
     */
    private void sendEmail(java.util.UUID patientId, String subject, String message) {
        try {
            // Use hardcoded email address for testing
            String email = HARDCODED_EMAIL_ADDRESS;
            
            // Prepare Email request
            EmailRequest emailRequest = new EmailRequest(email, subject, message);
            
            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Add authentication header if needed
            // headers.set("Authorization", "Bearer " + apiKey);
            
            HttpEntity<EmailRequest> requestEntity = new HttpEntity<>(emailRequest, headers);
            
            // Call SMU Lab Notification Service API
            String apiUrl = apiBaseUrl + sendEmailEndpoint;
            
            logger.info("📤 Sending Email to {} via {}", email, apiUrl);
            logger.debug("Request body: Email={}, Subject={}, Message={}", email, subject, message);
            
            ResponseEntity<EmailResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                EmailResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                EmailResponse emailResponse = response.getBody();
                if (emailResponse.isSuccess()) {
                    logger.info("✅ Email sent successfully to Patient {} (Email: {})", 
                        patientId, email);
                } else {
                    logger.warn("⚠️ Email API returned failure: {}", emailResponse.getMessage());
                }
            } else {
                logger.error("❌ Email API returned non-2xx status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("❌ Failed to send Email to Patient {}: {}", patientId, e.getMessage(), e);
            // Don't throw exception - notification failure shouldn't break queue operations
        }
    }
}
