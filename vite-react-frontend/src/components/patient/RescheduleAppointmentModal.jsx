import React, { useState, useEffect } from 'react';
import apiClient from '../../api/apiClient';
import SelectSlot from './SelectSlot';

const RescheduleAppointmentModal = ({ show, onHide, appointment, onSuccess }) => {
  const [selectedDate, setSelectedDate] = useState('');
  const [selectedTime, setSelectedTime] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  // Reset form when modal is opened/closed or appointment changes
  useEffect(() => {
    if (show && appointment) {
      // Clear previous selections when modal opens
      setSelectedDate('');
      setSelectedTime('');
      setError(null);
      setSuccess(false);
    }
  }, [show, appointment]);

  if (!show) return null;

  const canReschedule = () => {
    // Check if current appointment is at least 24 hours away
    if (!appointment?.startDatetime) return false;
    const appointmentTime = new Date(appointment.startDatetime);
    const now = new Date();
    const hoursDiff = (appointmentTime.getTime() - now.getTime()) / (1000 * 60 * 60);
    return hoursDiff >= 24;
  };

  const isNotReschedulable = !canReschedule();

  const handleReschedule = async () => {
    setError(null);
    
    // Validate that we're rescheduling at least 24h before the appointment
    if (!canReschedule()) {
      setError('You can only reschedule appointments that are at least 24 hours away.');
      return;
    }

    if (!selectedDate || !selectedTime) {
      setError('Please select a new date and time slot.');
      return;
    }

    try {
      setLoading(true);
      const newDateTime = `${selectedDate}T${selectedTime}:00`;
      await apiClient.put(`/api/appointments/${appointment.appointmentId}/reschedule`, null, { params: { newDateTime } });
      setSuccess(true);
      setTimeout(() => {
        setLoading(false);
        setSuccess(false);
        onSuccess && onSuccess();
        onHide && onHide();
      }, 1200);
    } catch (err) {
      console.error('Reschedule error:', err);
      const message = err.response?.data?.message || err.response?.data || err.message || 'Failed to reschedule. Please try again.';
      setError(typeof message === 'string' ? message : JSON.stringify(message));
      setLoading(false);
    }
  };

  return (
    <div className="modal show d-block" style={{ backgroundColor: 'rgba(0,0,0,0.4)', zIndex: 1060 }}>
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 shadow-lg">
          <div className="modal-header border-0">
            <div>
              <h5 className="modal-title mb-1">Reschedule Appointment</h5>
              <p className="text-muted small mb-0">Select a new time slot for your appointment</p>
            </div>
            <button type="button" className="btn-close" onClick={onHide}></button>
          </div>
          <div className="modal-body px-4">
            {success ? (
              <div className="alert alert-light border border-success d-flex align-items-center">
                <i className="bi bi-check-circle-fill text-success me-3" style={{ fontSize: '1.5rem' }}></i>
                <div>
                  <div className="fw-bold">Appointment Rescheduled Successfully</div>
                  <div className="small text-muted">Your appointment has been updated.</div>
                </div>
              </div>
            ) : (
              <div>
                {!canReschedule() ? (
                  <div className="alert alert-warning">
                    <i className="bi bi-exclamation-triangle me-2"></i>
                    This appointment is less than 24 hours away and cannot be rescheduled.
                  </div>
                ) : (
                  <>
                    {error && (
                      <div className="alert alert-danger border-0 d-flex align-items-start mb-3" role="alert">
                        <i className="bi bi-exclamation-circle me-2 mt-1"></i>
                        <div className="flex-grow-1">{error}</div>
                      </div>
                    )}

                    {/* Current Appointment Info */}
                    <div className="alert alert-light border mb-4">
                      <div className="small text-muted mb-1">Current Appointment</div>
                      <div className="d-flex justify-content-between align-items-start">
                        <div>
                          <div className="fw-semibold">{appointment?.clinicName || 'Unknown Clinic'}</div>
                          <div className="small">Dr. {appointment?.doctorName || 'Unknown Doctor'}</div>
                        </div>
                        <div className="text-end">
                          <div className="fw-semibold">
                            {new Date(appointment?.startDatetime).toLocaleDateString('en-US', {
                              month: 'short',
                              day: 'numeric',
                              year: 'numeric',
                            })}
                          </div>
                          <div className="small">
                            {new Date(appointment?.startDatetime).toLocaleTimeString('en-US', {
                              hour: '2-digit',
                              minute: '2-digit',
                            })}
                          </div>
                        </div>
                      </div>
                    </div>

                    {/* Select Slot Component */}
                    {appointment?.doctorId && appointment?.clinicName && (
                      <SelectSlot
                        selectedClinic={{ name: appointment.clinicName }}
                        selectedDoctor={{ doctorId: appointment.doctorId, name: appointment.doctorName }}
                        selectedDate={selectedDate}
                        selectedTime={selectedTime}
                        setSelectedDate={setSelectedDate}
                        setSelectedTime={setSelectedTime}
                      />
                    )}
                  </>
                )}
              </div>
            )}
          </div>
          <div className="modal-footer border-0 pt-0">
            {!success && !isNotReschedulable && (
              <>
                <button className="btn btn-light" onClick={onHide} disabled={loading}>Cancel</button>
                <button className="btn btn-primary" onClick={handleReschedule} disabled={loading || !selectedDate || !selectedTime}>
                  {loading ? (
                    <><span className="spinner-border spinner-border-sm me-2" role="status" />Rescheduling...</>
                  ) : (
                    'Confirm Reschedule'
                  )}
                </button>
              </>
            )}
            {!success && isNotReschedulable && (
              <button className="btn btn-light" onClick={onHide}>Close</button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RescheduleAppointmentModal;
