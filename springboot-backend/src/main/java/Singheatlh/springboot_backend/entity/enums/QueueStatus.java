package Singheatlh.springboot_backend.entity.enums;

public enum QueueStatus {
    // flow goes WAITING > NOTIFIED_3 > NOTIFIED_NEXT > CALLED > IN_CONSULTATION > COMPLETED
    // also can be WAITING > NOTIFIED_3 > NOTIFIED_NEXT > CALLED > NO_SHOW 

    WAITING,           // Patient checked in and waiting in queue
    NOTIFIED_3_AWAY,   // Patient notified they are 3 positions away (maybe can remve? also based on how descriptive u want)
    NOTIFIED_NEXT,     // Patient notified they are next (maybe can remve? also based on how descriptive u want)
    CALLED,            // Patient's number has been called
    IN_CONSULTATION,   // Patient is currently with the doctor
    COMPLETED,         // Patient has completed consultation
    NO_SHOW,           // Patient didn't show up when called
    CANCELLED,         // Queue ticket cancelled
    FAST_TRACKED       // Patient has been fast-tracked (priority)
}
