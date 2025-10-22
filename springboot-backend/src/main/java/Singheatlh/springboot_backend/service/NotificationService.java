package Singheatlh.springboot_backend.service;

import Singheatlh.springboot_backend.entity.QueueTicket;

/**
 * Service interface for sending notifications to patients
 * Integrated with SMU Lab Notification Service for SMS delivery
 */
public interface NotificationService {
    
    /**
     * Send notification when patient is 3 positions away from being called
     * @param queueTicket The queue ticket of the patient to notify
     */
    void sendQueueNotification3Away(QueueTicket queueTicket);
    
    /**
     * Send notification when patient is next in line
     * @param queueTicket The queue ticket of the patient to notify
     */
    void sendQueueNotificationNext(QueueTicket queueTicket);
    
    /**
     * Send notification when patient's queue number is called
     * @param queueTicket The queue ticket of the patient to notify
     */
    void sendQueueCalledNotification(QueueTicket queueTicket);
}
