package Singheatlh.springboot_backend.strategy.reschedule;

import Singheatlh.springboot_backend.dto.AppointmentDto;
import Singheatlh.springboot_backend.entity.Appointment;
import Singheatlh.springboot_backend.entity.enums.AppointmentStatus;
import Singheatlh.springboot_backend.mapper.AppointmentMapper;
import Singheatlh.springboot_backend.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract base class for appointment reschedule strategies.
 * Implements Template Method pattern to eliminate code duplication.
 * Common workflow is defined here, with strategy-specific validation delegated to subclasses.
 */
@RequiredArgsConstructor
public abstract class AbstractRescheduleStrategy implements AppointmentRescheduleStrategy {

    protected final AppointmentRepository appointmentRepository;
    protected final AppointmentMapper appointmentMapper;

    /**
     * Template method defining the reschedule workflow.
     * Subclasses customize behavior through the validateReschedule hook.
     */
    @Override
    public final AppointmentDto reschedule(Appointment appointment, RescheduleContext context) {
        // Hook method - allow subclasses to add specific validation
        validateReschedule(appointment, context);

        // Common logic - validate new time is not in the past
        validateNewTimeNotInPast(context);

        // Common logic - check for doctor conflicts
        validateNoDoctorConflicts(appointment, context);

        // Common logic - update appointment times
        updateAppointmentTimes(appointment, context);

        // Common logic - ensure status is Upcoming
        appointment.setStatus(AppointmentStatus.Upcoming);

        // Persist and return DTO
        Appointment updatedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(updatedAppointment);
    }

    /**
     * Hook method for subclasses to implement strategy-specific validation.
     * Each strategy (Patient, Staff) has different reschedule rules.
     *
     * @param appointment The appointment to validate
     * @param context The reschedule context
     * @throws IllegalArgumentException if reschedule fails validation
     */
    protected abstract void validateReschedule(Appointment appointment, RescheduleContext context);

    /**
     * Validate that the new appointment time is not in the past.
     * Common validation shared by all reschedule strategies.
     */
    private void validateNewTimeNotInPast(RescheduleContext context) {
        if (context.getNewDateTime().isBefore(context.getNow())) {
            throw new IllegalArgumentException("New appointment time cannot be in the past");
        }
    }

    /**
     * Check for doctor conflicts at the new time.
     * Common validation shared by all reschedule strategies.
     */
    private void validateNoDoctorConflicts(Appointment appointment, RescheduleContext context) {
        // Calculate new end time based on current appointment duration
        long durationMinutes = Duration.between(
            appointment.getStartDatetime(),
            appointment.getEndDatetime()
        ).toMinutes();
        LocalDateTime newEndTime = context.getNewDateTime().plusMinutes(durationMinutes);

        // Check for conflicts with other appointments for the same doctor
        List<Appointment> conflicts = appointmentRepository
            .findByDoctorIdAndStartDatetimeBetween(
                appointment.getDoctorId(),
                context.getNewDateTime().minusMinutes(30),
                newEndTime
            )
            .stream()
            .filter(apt -> apt.getStatus() == AppointmentStatus.Upcoming || apt.getStatus() == AppointmentStatus.Ongoing)
            .filter(apt -> !apt.getAppointmentId().equals(appointment.getAppointmentId())) // Exclude current appointment
            .collect(Collectors.toList());

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("Doctor is not available at the requested time");
        }
    }

    /**
     * Update the appointment start and end times.
     * Common logic shared by all reschedule strategies.
     */
    private void updateAppointmentTimes(Appointment appointment, RescheduleContext context) {
        // Calculate duration to preserve it
        long durationMinutes = Duration.between(
            appointment.getStartDatetime(),
            appointment.getEndDatetime()
        ).toMinutes();

        // Update times
        appointment.setStartDatetime(context.getNewDateTime());
        appointment.setEndDatetime(context.getNewDateTime().plusMinutes(durationMinutes));
    }
}
