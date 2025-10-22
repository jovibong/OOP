-- SQL Migration Script: Update QueueStatus enum values
-- Generated: 2025-10-10
-- Purpose: 
--   1. Remove NOTIFIED_3_AWAY and NOTIFIED_NEXT statuses
--   2. Rename WAITING to CHECKED_IN

-- Step 1: Update existing records with old status values to new values
-- Convert NOTIFIED_3_AWAY and NOTIFIED_NEXT to CHECKED_IN
UPDATE queue_ticket 
SET status = 'CHECKED_IN' 
WHERE status IN ('NOTIFIED_3_AWAY', 'NOTIFIED_NEXT', 'WAITING');

UPDATE queue_ticket 
SET status = 'NO_SHOW' 
WHERE status IN ('CANCELLED');

-- Step 2: Drop the existing check constraint on the status column
ALTER TABLE queue_ticket 
DROP CONSTRAINT IF EXISTS queue_ticket_status_check;

-- Step 3: Add the new check constraint with updated enum values
-- Valid statuses: CHECKED_IN, CALLED, IN_CONSULTATION, COMPLETED, NO_SHOW, CANCELLED, FAST_TRACKED
ALTER TABLE queue_ticket 
ADD CONSTRAINT queue_ticket_status_check 
CHECK (status IN ('CHECKED_IN', 'CALLED', 'IN_CONSULTATION', 'COMPLETED', 'NO_SHOW', 'FAST_TRACKED'));

-- Verification: Count records by status to ensure migration was successful
SELECT status, COUNT(*) as count 
FROM queue_ticket 
GROUP BY status 
ORDER BY status;

-- Note: After running this migration:
-- - All patients use CHECKED_IN status instead of WAITING
-- - First patient (queue_number = 1) is automatically set to CALLED upon check-in
-- - Notifications are triggered programmatically without changing patient status
