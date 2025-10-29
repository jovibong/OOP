-- =====================================================
-- Database Population Script for SingHealth Clinic System
-- Description: Populates tables with sample data from CSV files
-- Requirements: CSV files must be accessible at /sample-data/ in PostgreSQL container
-- =====================================================

-- =====================================================
-- 1. POPULATE CLINIC TABLE
-- Description: Load clinic data from clinics.csv (~1700 clinics)
-- Note: clinic_id is auto-generated (SERIAL), CSV doesn't include it
-- =====================================================

-- Create temporary table to load CSV (without clinic_id)
CREATE TEMPORARY TABLE temp_clinic (
    name VARCHAR(255),
    address VARCHAR(255),
    telephone_number VARCHAR(20),
    type VARCHAR(1),
    opening_hours TIME,
    closing_hours TIME
);

-- Load data from CSV into temporary table
COPY temp_clinic(name, address, telephone_number, type, opening_hours, closing_hours)
FROM '/sample-data/clinics.csv'
DELIMITER ','
CSV HEADER;

-- Insert from temporary table into actual table
INSERT INTO Clinic (name, address, telephone_number, type, opening_hours, closing_hours)
SELECT name, address, telephone_number, type, opening_hours, closing_hours
FROM temp_clinic;

-- Clean up temporary table
DROP TABLE temp_clinic;

-- =====================================================
-- 2. POPULATE USER_PROFILE TABLE
-- Description: Load user data from user_profile.csv (~3300 users)
-- Includes: 1 system admin, ~2500 clinic staff, ~800 patients
-- =====================================================

-- Create temporary table for user profiles
CREATE TEMPORARY TABLE temp_user_profile (
    user_id UUID,
    name VARCHAR(255),
    role VARCHAR(1),
    email VARCHAR(255),
    telephone_number VARCHAR(20),
    clinic_id TEXT  -- TEXT to handle empty strings
);

-- Load data from CSV
COPY temp_user_profile(user_id, name, role, email, telephone_number, clinic_id)
FROM '/sample-data/user_profile.csv'
DELIMITER ','
CSV HEADER
NULL '';  -- Handle empty clinic_id as NULL

-- Insert from temporary table, converting empty string to NULL for clinic_id
INSERT INTO User_Profile (user_id, name, role, email, telephone_number, clinic_id)
SELECT
    user_id,
    name,
    role,
    email,
    telephone_number,
    CASE WHEN clinic_id = '' THEN NULL ELSE clinic_id::INT END
FROM temp_user_profile;

-- Clean up
DROP TABLE temp_user_profile;

-- =====================================================
-- 3. POPULATE DOCTOR TABLE
-- Description: Load doctor data from doctor.csv (~6000 doctors)
-- =====================================================

COPY Doctor(doctor_id, name, clinic_id)
FROM '/sample-data/doctor.csv'
DELIMITER ','
CSV HEADER;

-- =====================================================
-- 4. POPULATE SCHEDULE TABLE
-- Description: Load schedule data from schedule.csv (~600K schedules)
-- Covers: Oct 22, 2025 through Nov 15, 2025
-- =====================================================

COPY Schedule(schedule_id, doctor_id, start_datetime, end_datetime, type)
FROM '/sample-data/schedule.csv'
DELIMITER ','
CSV HEADER;

-- =====================================================
-- 5. POPULATE APPOINTMENT TABLE
-- Description: Load appointment data from appointment.csv (5000 appointments)
-- =====================================================

COPY Appointment(appointment_id, patient_id, doctor_id, start_datetime, end_datetime, status)
FROM '/sample-data/appointment.csv'
DELIMITER ','
CSV HEADER;

-- =====================================================
-- 6. POPULATE MEDICAL_SUMMARY TABLE
-- Description: Load medical summaries from medical_summary.csv (~1200 summaries)
-- Note: Only for completed appointments
-- =====================================================

COPY Medical_Summary(summary_id, appointment_id, treatment_summary)
FROM '/sample-data/medical_summary.csv'
DELIMITER ','
CSV HEADER;

-- =====================================================
-- 7. POPULATE QUEUE_TICKET TABLE
-- Description: Load queue tickets from queue_ticket.csv (~200 tickets)
-- Note: Only for today's appointments (2025-10-29)
-- =====================================================

-- Create temporary table to handle empty fast_track_reason
CREATE TEMPORARY TABLE temp_queue_ticket (
    ticket_id INTEGER,
    appointment_id CHAR(10),
    status VARCHAR(20),
    check_in_time TIMESTAMP,
    queue_number INTEGER,
    is_fast_tracked BOOLEAN,
    fast_track_reason TEXT
);

-- Load data from CSV
COPY temp_queue_ticket(ticket_id, appointment_id, status, check_in_time, queue_number, is_fast_tracked, fast_track_reason)
FROM '/sample-data/queue_ticket.csv'
DELIMITER ','
CSV HEADER
NULL '';

-- Insert from temporary table, converting empty fast_track_reason to NULL
INSERT INTO Queue_Ticket (ticket_id, appointment_id, status, check_in_time, queue_number, is_fast_tracked, fast_track_reason)
SELECT
    ticket_id,
    appointment_id,
    status,
    check_in_time,
    queue_number,
    is_fast_tracked,
    NULLIF(fast_track_reason, '')
FROM temp_queue_ticket;

-- Clean up
DROP TABLE temp_queue_ticket;

-- Update the SERIAL sequence to continue from the last inserted ticket_id
SELECT setval('queue_ticket_ticket_id_seq', (SELECT MAX(ticket_id) FROM Queue_Ticket));

-- =====================================================
-- 8. VERIFICATION QUERIES
-- Description: Verify the data has been inserted correctly
-- =====================================================

-- Count records in each table
SELECT 'Clinic' as table_name, COUNT(*) as record_count FROM Clinic
UNION ALL
SELECT 'User_Profile' as table_name, COUNT(*) as record_count FROM User_Profile
UNION ALL
SELECT 'Doctor' as table_name, COUNT(*) as record_count FROM Doctor
UNION ALL
SELECT 'Schedule' as table_name, COUNT(*) as record_count FROM Schedule
UNION ALL
SELECT 'Appointment' as table_name, COUNT(*) as record_count FROM Appointment
UNION ALL
SELECT 'Medical_Summary' as table_name, COUNT(*) as record_count FROM Medical_Summary
UNION ALL
SELECT 'Queue_Ticket' as table_name, COUNT(*) as record_count FROM Queue_Ticket;

-- Show sample data from each table
SELECT '=== CLINIC SAMPLE ===' as info;
SELECT clinic_id, type, name, telephone_number FROM Clinic LIMIT 3;

SELECT '=== USER SAMPLE ===' as info;
SELECT user_id, name, email, role FROM User_Profile LIMIT 3;

SELECT '=== DOCTOR SAMPLE ===' as info;
SELECT doctor_id, name, clinic_id FROM Doctor LIMIT 3;

SELECT '=== SCHEDULE SAMPLE ===' as info;
SELECT schedule_id, doctor_id, start_datetime, type FROM Schedule LIMIT 3;

SELECT '=== APPOINTMENT SAMPLE ===' as info;
SELECT appointment_id, patient_id, doctor_id, start_datetime, status FROM Appointment LIMIT 3;

SELECT '=== MEDICAL SUMMARY SAMPLE ===' as info;
SELECT summary_id, appointment_id, LEFT(treatment_summary, 50) as summary_preview FROM Medical_Summary LIMIT 3;

SELECT '=== QUEUE TICKET SAMPLE ===' as info;
SELECT ticket_id, appointment_id, status, check_in_time, queue_number FROM Queue_Ticket LIMIT 3;

-- =====================================================
-- END OF POPULATION SCRIPT
-- =====================================================
