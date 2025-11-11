package Singheatlh.springboot_backend.strategy.reschedule;

import Singheatlh.springboot_backend.entity.Appointment;
import Singheatlh.springboot_backend.entity.enums.AppointmentStatus;
import Singheatlh.springboot_backend.mapper.AppointmentMapper;
import Singheatlh.springboot_backend.repository.AppointmentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Strategy for patient-initiated appointment rescheduling.
 * Enforces patient-specific reschedule rules:
 * - Must reschedule at least 24 hours in advance
 * - Cannot reschedule to same day (must be at least tomorrow)
 * - Cannot have multiple appointments on the same day
 */
@Component
public class PatientRescheduleStrategy extends AbstractRescheduleStrategy {

    public PatientRescheduleStrategy(AppointmentRepository appointmentRepository,
                                    AppointmentMapper appointmentMapper) {
        super(appointmentRepository, appointmentMapper);
    }

    @Override
    public String getStrategyName() {
        return "PATIENT";
    }

    @Override
    protected void validateReschedule(Appointment appointment, RescheduleContext context) {
        LocalDateTime currentAppointmentTime = appointment.getStartDatetime();
        LocalDateTime now = context.getNow();
        LocalDateTime newDateTime = context.getNewDateTime();

        // Rule 1: Must reschedule at least 24 hours in advance
        if (currentAppointmentTime.isBefore(now.plusHours(24))) {
            throw new IllegalArgumentException(
                "Cannot reschedule appointments less than 24 hours in advance"
            );
        }

        // Rule 2: New appointment must be at least one day in advance (not same day)
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        LocalDateTime tomorrow = today.plusDays(1);
        if (newDateTime.isBefore(tomorrow)) {
            throw new IllegalArgumentException(
                "Appointments must be rescheduled to at least one day in advance. " +
                "Please select a date from tomorrow onwards."
            );
        }

        // Rule 3: Patient cannot have multiple appointments on the same day
        validateOneAppointmentPerDay(appointment, newDateTime);
    }

    /**
     * Validate that the patient doesn't already have an appointment on the new date.
     */
    private void validateOneAppointmentPerDay(Appointment appointment, LocalDateTime newDateTime) {
        LocalDateTime startOfDay = newDateTime.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        List<Appointment> patientAppointmentsOnDay = appointmentRepository
            .findByPatientIdAndStartDatetimeBetween(appointment.getPatientId(), startOfDay, endOfDay)
            .stream()
            .filter(apt -> !apt.getAppointmentId().equals(appointment.getAppointmentId())) // Exclude current appointment
            .filter(apt -> apt.getStatus() == AppointmentStatus.Upcoming || apt.getStatus() == AppointmentStatus.Ongoing)
            .collect(Collectors.toList());

        if (!patientAppointmentsOnDay.isEmpty()) {
            throw new IllegalArgumentException(
                "You already have an appointment scheduled on this day. Please choose a different date."
            );
        }
    }
}
