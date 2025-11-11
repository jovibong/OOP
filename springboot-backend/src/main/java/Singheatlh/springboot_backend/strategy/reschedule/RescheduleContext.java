package Singheatlh.springboot_backend.strategy.reschedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Context object containing all information needed for appointment rescheduling.
 * Encapsulates reschedule parameters to avoid long parameter lists.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RescheduleContext {

    /**
     * The new appointment date and time.
     */
    private LocalDateTime newDateTime;

    /**
     * Flag indicating if the reschedule is being performed by staff.
     * Used to select the appropriate reschedule strategy.
     */
    private boolean isStaff;

    /**
     * Current timestamp for validation purposes.
     * Allows for consistent time checking and testability.
     */
    private LocalDateTime now;
}
