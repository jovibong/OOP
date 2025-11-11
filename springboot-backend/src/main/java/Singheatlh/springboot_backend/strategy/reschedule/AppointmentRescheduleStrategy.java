package Singheatlh.springboot_backend.strategy.reschedule;

import Singheatlh.springboot_backend.dto.AppointmentDto;
import Singheatlh.springboot_backend.entity.Appointment;

/**
 * Strategy interface for different appointment reschedule types.
 * Following Strategy Pattern and Open/Closed Principle.
 * New reschedule rules can be added by implementing this interface.
 */
public interface AppointmentRescheduleStrategy {

    /**
     * Get the name of this strategy for identification.
     * @return Strategy name (e.g., "PATIENT", "STAFF")
     */
    String getStrategyName();

    /**
     * Reschedule an appointment using this strategy's specific rules.
     * @param appointment The appointment to reschedule
     * @param context The reschedule context (new time, who is rescheduling, etc.)
     * @return The rescheduled appointment DTO
     * @throws IllegalArgumentException if reschedule fails validation
     */
    AppointmentDto reschedule(Appointment appointment, RescheduleContext context);
}
