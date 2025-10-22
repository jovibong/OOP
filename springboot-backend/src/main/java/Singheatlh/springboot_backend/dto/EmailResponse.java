package Singheatlh.springboot_backend.dto;

/**
 * DTO for Email response from SMU Lab Notification Service
 */
public class EmailResponse {
    
    private boolean success;
    private String message;
    private String messageId;
    
    public EmailResponse() {
    }
    
    public EmailResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public EmailResponse(boolean success, String message, String messageId) {
        this.success = success;
        this.message = message;
        this.messageId = messageId;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getMessageId() {
        return messageId;
    }
    
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
    
    @Override
    public String toString() {
        return "EmailResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

