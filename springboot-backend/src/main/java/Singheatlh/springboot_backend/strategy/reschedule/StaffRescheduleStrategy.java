package Singheatlh.springboot_backend.strategy.reschedule;

import Singheatlh.springboot_backend.entity.Appointment;
import Singheatlh.springboot_backend.mapper.AppointmentMapper;
import Singheatlh.springboot_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Component;

/**
 * Strategy for staff-initiated appointment rescheduling.
 * Enforces staff-specific reschedule rules:
 * - Can reschedule at any time (no 24-hour restriction)
 * - Can reschedule to same day
 * - No one-appointment-per-day restriction
 * - Only validates doctor availability and no past time
 */
@Component
public class StaffRescheduleStrategy extends AbstractRescheduleStrategy {

    public StaffRescheduleStrategy(AppointmentRepository appointmentRepository,
                                  AppointmentMapper appointmentMapper) {
        super(appointmentRepository, appointmentMapper);
    }

    @Override
    public String getStrategyName() {
        return "STAFF";
    }

    @Override
    protected void validateReschedule(Appointment appointment, RescheduleContext context) {
        // No additional validation needed for staff
        // Staff has full flexibility to reschedule appointments:
        // - Can reschedule less than 24 hours before appointment
        // - Can reschedule to same day
        // - Can reschedule to any future time
        // - No restriction on multiple appointments per day

        // Base class already handles:
        // - New time must be in the future (not past)
        // - Doctor must be available (no conflicts)
    }
}
