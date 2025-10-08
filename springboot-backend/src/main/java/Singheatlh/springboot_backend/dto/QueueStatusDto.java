package Singheatlh.springboot_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueueStatusDto {
    
    private Long ticketId;
    private Integer queueNumber;
    private Integer currentQueueNumber;
    private Integer positionInQueue;
    private String patientName;
    private String doctorName;
    private String clinicName;
    private String status;
    private String message;
}
