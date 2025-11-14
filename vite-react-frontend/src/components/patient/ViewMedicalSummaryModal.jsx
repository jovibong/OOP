import React, { useEffect } from 'react';

const ViewMedicalSummaryModal = ({ show, onHide, appointment, summary, loading }) => {
  // Add print styles when component mounts
  useEffect(() => {
    if (!show) return;
    
    const styleId = 'medical-summary-print-styles';
    if (!document.getElementById(styleId)) {
      const style = document.createElement('style');
      style.id = styleId;
      style.textContent = `
        @media print {
          @page {
            margin: 1cm;
            size: A4 portrait;
          }
          
          /* Hide everything on page except modal */
          body * {
            visibility: hidden;
            height: 0;
            overflow: hidden;
          }
          
          /* Show only modal content */
          .modal, .modal * {
            visibility: visible;
            height: auto;
            overflow: visible;
          }
          
          /* Position modal for print */
          .modal {
            position: absolute !important;
            left: 0 !important;
            top: 0 !important;
            background: white !important;
            display: block !important;
            height: auto !important;
            min-height: 0 !important;
            max-height: none !important;
          }
          
          .modal-dialog {
            max-width: 100% !important;
            margin: 0 !important;
            transform: none !important;
            height: auto !important;
          }
          
          .modal-content {
            border: none !important;
            box-shadow: none !important;
            border-radius: 0 !important;
            max-height: none !important;
            height: auto !important;
          }
          
          /* Hide buttons and backdrop */
          .no-print {
            visibility: hidden !important;
            display: none !important;
            height: 0 !important;
          }
          
          /* Force single page - make content fit */
          .modal-body {
            max-height: none !important;
            overflow: visible !important;
            height: auto !important;
          }
          
          .modal-header, .modal-footer {
            height: auto !important;
          }
          
          /* Prevent page breaks */
          .modal-content,
          .card,
          .card-body {
            page-break-inside: avoid;
            page-break-before: auto;
            page-break-after: avoid;
          }
          
          /* Remove any extra spacing */
          body {
            margin: 0 !important;
            padding: 0 !important;
          }
          
          html {
            height: auto !important;
          }
        }
      `;
      document.head.appendChild(style);
    }
  }, [show]);

  // Close on Escape key
  useEffect(() => {
    if (!show) return;
    
    const handleEscape = (e) => {
      if (e.key === 'Escape') {
        onHide();
      }
    };
    
    document.addEventListener('keydown', handleEscape);
    return () => document.removeEventListener('keydown', handleEscape);
  }, [show, onHide]);

  if (!show) return null;

  const formatDate = (dateTimeString) => {
    if (!dateTimeString) return 'N/A';
    const date = new Date(dateTimeString);
    return date.toLocaleDateString('en-US', {
      weekday: 'long',
      year: 'numeric',
      month: 'long',
      day: 'numeric',
    });
  };

  const formatTime = (dateTimeString) => {
    if (!dateTimeString) return 'N/A';
    const date = new Date(dateTimeString);
    return date.toLocaleTimeString('en-US', {
      hour: '2-digit',
      minute: '2-digit',
      timeZone: 'Asia/Singapore',
    });
  };

  return (
    <div 
      className="modal show d-block" 
      style={{ backgroundColor: 'rgba(0,0,0,0.5)', zIndex: 1060 }}
      onClick={(e) => {
        if (e.target === e.currentTarget) onHide();
      }}
      role="dialog"
      aria-modal="true"
      aria-labelledby="medical-summary-title"
    >
      <div className="modal-dialog modal-dialog-centered modal-lg">
        <div className="modal-content border-0 shadow-lg" style={{ borderRadius: '12px' }}>
          {/* Header with Medical Document Style */}
          <div className="modal-header border-0 pb-2" style={{ backgroundColor: '#f8f9fa' }}>
            <div className="w-100">
              <div className="d-flex justify-content-between align-items-start">
                <div>
                  <h5 id="medical-summary-title" className="modal-title mb-1 fw-bold" style={{ color: '#2c3e50' }}>
                    <i className="bi bi-file-medical me-2 text-primary"></i>
                    Medical Summary
                  </h5>
                  <p className="text-muted small mb-0">
                    Confidential Patient Record
                  </p>
                </div>
                <button 
                  type="button" 
                  className="btn-close no-print" 
                  onClick={onHide}
                  aria-label="Close medical summary"
                ></button>
              </div>
            </div>
          </div>

          <div className="modal-body px-4 py-4">
            {/* Appointment Details Card */}
            <div className="card border-0 mb-4" style={{ backgroundColor: '#f8f9fa' }}>
              <div className="card-body p-3">
                <div className="row g-3">
                  <div className="col-md-6">
                    <div className="mb-2">
                      <label className="text-muted small fw-semibold d-block mb-1">
                        <i className="bi bi-calendar-check me-1"></i> Appointment Date
                      </label>
                      <div className="fw-semibold">{formatDate(appointment?.startDatetime)}</div>
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="mb-2">
                      <label className="text-muted small fw-semibold d-block mb-1">
                        <i className="bi bi-clock me-1"></i> Time
                      </label>
                      <div className="fw-semibold">{formatTime(appointment?.startDatetime)}</div>
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="mb-2">
                      <label className="text-muted small fw-semibold d-block mb-1">
                        <i className="bi bi-hospital me-1"></i> Clinic
                      </label>
                      <div className="fw-semibold">{appointment?.clinicName || 'N/A'}</div>
                    </div>
                  </div>
                  <div className="col-md-6">
                    <div className="mb-2">
                      <label className="text-muted small fw-semibold d-block mb-1">
                        <i className="bi bi-person-badge me-1"></i> Doctor
                      </label>
                      <div className="fw-semibold">{appointment?.doctorName || 'N/A'}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            {/* Treatment Summary Section */}
            <div className="mb-3">
              <h6 className="fw-bold mb-3" style={{ color: '#2c3e50' }}>
                <i className="bi bi-journal-medical me-2 text-success"></i>
                Treatment Summary
              </h6>
              
              {loading ? (
                <div className="text-center py-5">
                  <div className="spinner-border text-primary" role="status">
                    <span className="visually-hidden">Loading summary...</span>
                  </div>
                  <p className="text-muted mt-3 mb-0">Loading medical summary...</p>
                </div>
              ) : summary ? (
                <div 
                  className="border rounded p-4" 
                  style={{ 
                    backgroundColor: '#ffffff',
                    lineHeight: '1.8',
                    minHeight: '150px'
                  }}
                >
                  <p className="mb-0" style={{ whiteSpace: 'pre-wrap', fontSize: '0.95rem' }}>
                    {summary.treatmentSummary}
                  </p>
                </div>
              ) : (
                <div className="alert alert-info border-0 d-flex align-items-center">
                  <i className="bi bi-info-circle me-2" style={{ fontSize: '1.5rem' }}></i>
                  <div>
                    <strong>No Summary Available</strong>
                    <p className="mb-0 small">
                      The medical summary for this appointment has not been completed yet. 
                      Please contact your doctor if you have questions about your visit.
                    </p>
                  </div>
                </div>
              )}
            </div>

            {/* Summary ID Footer (if available) */}
            {summary && (
              <div className="text-muted small text-end mt-3 pt-2 border-top">
                <i className="bi bi-file-text me-1"></i>
                Summary ID: <span className="font-monospace">{summary.summaryId}</span>
              </div>
            )}
          </div>

          <div className="modal-footer border-0 pt-0 pb-3 no-print">
            <button 
              className="btn btn-light px-4" 
              onClick={onHide}
              aria-label="Close modal"
            >
              <i className="bi bi-x-lg me-1"></i>
              Close
            </button>
            {summary && (
              <button 
                className="btn btn-outline-primary px-4"
                onClick={() => window.print()}
                aria-label="Print medical summary"
              >
                <i className="bi bi-printer me-1"></i>
                Print
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ViewMedicalSummaryModal;
