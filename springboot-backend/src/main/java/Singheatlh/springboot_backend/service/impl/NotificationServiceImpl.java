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

import Singheatlh.springboot_backend.dto.SmsRequest;
import Singheatlh.springboot_backend.dto.SmsResponse;
import Singheatlh.springboot_backend.entity.QueueTicket;
import Singheatlh.springboot_backend.service.NotificationService;

/**
 * Implementation of NotificationService
 * Integrated with SMU Lab Notification Service for sending SMS
 * Currently uses hardcoded phone number for testing
 */
@Service
public class NotificationServiceImpl implements NotificationService {
    
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    
    // Hardcoded phone number for testing
    private static final String HARDCODED_PHONE_NUMBER = "+6594653483";
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Value("${smu.notification.api.base-url}")
    private String apiBaseUrl;
    
    @Value("${smu.notification.api.send-sms-endpoint}")
    private String sendSmsEndpoint;
    
    @Override
    public void sendQueueNotification3Away(QueueTicket queueTicket) {
        String message = String.format(
            "Queue #%d: You are currently 3 patients away. " +
            "Please proceed closer to the consultation room.",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📱 NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), message);
        
        // Send SMS via SMU Lab Notification Service
        sendSms(queueTicket.getPatientId(), message);
    }
    
    @Override
    public void sendQueueNotificationNext(QueueTicket queueTicket) {
        String message = String.format(
            "Queue Number #%d: You are next. Please be ready.",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📱 NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), message);
        
        // Send SMS via SMU Lab Notification Service
        sendSms(queueTicket.getPatientId(), message);
    }
    
    @Override
    public void sendQueueCalledNotification(QueueTicket queueTicket) {
        String message = String.format(
            "Queue #%d: It's your turn. Kindly enter the Room [Number]",
            queueTicket.getQueueNumber()
        );
        
        logger.info("📱 NOTIFICATION [Patient ID: {}, Queue #{}]: {}", 
            queueTicket.getPatientId(), queueTicket.getQueueNumber(), message);
        
        // Send SMS via SMU Lab Notification Service
        sendSms(queueTicket.getPatientId(), message);
    }
    
    /**
     * Send SMS using SMU Lab Notification Service API
     * Currently uses hardcoded phone number for testing
     * @param patientId Patient ID (for logging purposes)
     * @param message SMS message content
     */
    private void sendSms(Long patientId, String message) {
        try {
            // Use hardcoded phone number for testing
            String phoneNumber = HARDCODED_PHONE_NUMBER;
            
            // Prepare SMS request
            SmsRequest smsRequest = new SmsRequest(phoneNumber, message);
            
            // Prepare HTTP headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Add authentication header if needed
            // headers.set("Authorization", "Bearer " + apiKey);
            
            HttpEntity<SmsRequest> requestEntity = new HttpEntity<>(smsRequest, headers);
            
            // Call SMU Lab Notification Service API
            String apiUrl = apiBaseUrl + sendSmsEndpoint;
            
            logger.info("📤 Sending SMS to {} via {}", phoneNumber, apiUrl);
            logger.debug("Request body: PhoneNumber={}, Message={}", phoneNumber, message);
            
            ResponseEntity<SmsResponse> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                requestEntity,
                SmsResponse.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                SmsResponse smsResponse = response.getBody();
                if (smsResponse.isSuccess()) {
                    logger.info("✅ SMS sent successfully to Patient {} (Phone: {})", 
                        patientId, phoneNumber);
                } else {
                    logger.warn("⚠️ SMS API returned failure: {}", smsResponse.getMessage());
                }
            } else {
                logger.error("❌ SMS API returned non-2xx status: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            logger.error("❌ Failed to send SMS to Patient {}: {}", patientId, e.getMessage(), e);
            // Don't throw exception - notification failure shouldn't break queue operations
        }
    }
}
