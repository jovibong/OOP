-- =====================================================
-- Migration: Add ticket_number_for_day column to Queue_Ticket
-- Description: Adds a daily sequential ticket number per clinic
-- =====================================================

ALTER TABLE Queue_Ticket 
ADD COLUMN ticket_number_for_day INTEGER;

-- Add index for performance when querying max ticket number for day
CREATE INDEX idx_queue_ticket_clinic_date ON Queue_Ticket(check_in_time);

-- Add comment for documentation
COMMENT ON COLUMN Queue_Ticket.ticket_number_for_day IS 'Daily sequential ticket number starting from 1 for each clinic';

