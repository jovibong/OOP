package Singheatlh.springboot_backend.dto;

/**
 * DTO for SMS response from SMU Lab Notification Service
 */
public class SmsResponse {
    
    private boolean success;
    private String message;
    private String messageId;
    
    public SmsResponse() {
    }
    
    public SmsResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public SmsResponse(boolean success, String message, String messageId) {
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
        return "SmsResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}

