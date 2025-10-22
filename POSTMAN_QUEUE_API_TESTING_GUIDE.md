# Postman API Testing Guide - Queue Management Endpoints

## Base URL
```
http://localhost:8080
```
*(Adjust port if your Spring Boot app runs on a different port)*

---

## Prerequisites

Before testing Queue endpoints, ensure you have:
1. ✅ A valid **Appointment** in the database (with `appointment_id`)
2. ✅ The appointment status should be `SCHEDULED` or `CONFIRMED`
3. ✅ Valid `doctor_id`, `clinic_id`, and `patient_id` associated with the appointment

---

## Testing Workflow (Recommended Order)

### 📋 Phase 1: Patient Check-In
### 📋 Phase 2: Queue Monitoring
### 📋 Phase 3: Staff Management
### 📋 Phase 4: Cleanup/Cancel

---

## 🔵 PHASE 1: PATIENT CHECK-IN

### 1. Check-In Patient (Create Queue Ticket)
**Creates a queue ticket when patient arrives at clinic**

```
Method: POST
URL: http://localhost:8080/api/queue/check-in/{appointmentId}
```

**Path Variables:**
- `appointmentId` = `1` (use actual appointment ID from your database)

**Headers:**
```
Content-Type: application/json
```

**Request Body:** *(None required)*

**Sample Response (201 CREATED):**
```json
{
    "ticketId": 1,
    "appointmentId": 1,
    "status": "WAITING",
    "checkInTime": "2025-10-08T14:30:00",
    "queueNumber": 1,
    "clinicId": 101,
    "clinicName": null,
    "doctorId": 201,
    "doctorName": null,
    "patientId": 301,
    "patientName": "John Doe",
    "isFastTracked": false,
    "fastTrackReason": null,
    "appointmentDatetime": "2025-10-08T15:00:00"
}
```

**Possible Errors:**
- `400 BAD REQUEST` - Appointment already checked in
- `400 BAD REQUEST` - Appointment status invalid (not UPCOMING/CONFIRMED)
- `404 NOT FOUND` - Appointment doesn't exist

---

## 🟢 PHASE 2: QUEUE MONITORING

### 2. Get Queue Ticket by Ticket ID
**Retrieve queue ticket details**

```
Method: GET
URL: http://localhost:8080/api/queue/ticket/{ticketId}
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "appointmentId": 1,
    "status": "WAITING",
    "queueNumber": 1,
    "checkInTime": "2025-10-08T14:30:00",
    "clinicId": 101,
    "doctorId": 201,
    "patientId": 301,
    "patientName": "John Doe",
    "isFastTracked": false,
    "fastTrackReason": null
}
```

---

### 3. Get Queue Ticket by Appointment ID
**Find queue ticket using appointment ID**

```
Method: GET
URL: http://localhost:8080/api/queue/appointment/{appointmentId}
```

**Path Variables:**
- `appointmentId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):** *(Same as endpoint #2)*

---

### 4. Get Real-Time Queue Status
**Track patient's position in queue**

```
Method: GET
URL: http://localhost:8080/api/queue/status/{ticketId}
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "queueNumber": 5,
    "currentQueueNumber": 2,
    "positionInQueue": 3,
    "patientName": null,
    "doctorName": null,
    "clinicName": null,
    "status": "WAITING",
    "message": "You are Queue #5, currently serving #2"
}
```

**Possible Messages:**
- `"You are 2 patient(s) away. Please proceed closer to the consultation room."`
- `"It's your turn. Kindly enter the consultation room."`
- `"You are currently with the doctor."`

---

### 5. Get Active Queue by Doctor
**View all active patients for a specific doctor**

```
Method: GET
URL: http://localhost:8080/api/queue/doctor/{doctorId}
```

**Path Variables:**
- `doctorId` = `201`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
[
    {
        "ticketId": 1,
        "queueNumber": 1,
        "status": "WAITING",
        "patientName": "John Doe",
        "isFastTracked": false
    },
    {
        "ticketId": 2,
        "queueNumber": 2,
        "status": "WAITING",
        "patientName": "Jane Smith",
        "isFastTracked": false
    }
]
```

---

### 6. Get Active Queue by Clinic
**View all active patients for entire clinic**

```
Method: GET
URL: http://localhost:8080/api/queue/clinic/{clinicId}
```

**Path Variables:**
- `clinicId` = `101`

**Request Body:** *(None)*

**Sample Response (200 OK):** *(Array of queue tickets)*

---

### 7. Get Current Serving Number
**Get the queue number currently being served**

```
Method: GET
URL: http://localhost:8080/api/queue/current/{doctorId}
```

**Path Variables:**
- `doctorId` = `201`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "currentQueueNumber": 3
}
```

---

### 8. Get Active Queue Count
**Count of patients waiting**

```
Method: GET
URL: http://localhost:8080/api/queue/count/{doctorId}
```

**Path Variables:**
- `doctorId` = `201`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "activeCount": 5
}
```

---

### 9. Get Patient's Queue History
**Get all queue tickets for a patient**

```
Method: GET
URL: http://localhost:8080/api/queue/patient/{patientId}
```

**Path Variables:**
- `patientId` = `301`

**Request Body:** *(None)*

**Sample Response (200 OK):** *(Array of all queue tickets for patient)*

---

## 🟡 PHASE 3: STAFF MANAGEMENT

### 10. Call Next Queue Number
**Staff calls next patient (auto-completes previous patient)**

```
Method: POST
URL: http://localhost:8080/api/queue/call-next/{doctorId}
```

**Path Variables:**
- `doctorId` = `201`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 2,
    "queueNumber": 2,
    "status": "CALLED",
    "patientName": "Jane Smith"
}
```

**Possible Errors:**
- `400 BAD REQUEST` - No patients in queue
- `400 BAD REQUEST` - No waiting patients

---

### 11. Update Queue Status (Generic)
**Update queue ticket to any status**

```
Method: PUT
URL: http://localhost:8080/api/queue/ticket/{ticketId}/status?status=IN_CONSULTATION
```

**Path Variables:**
- `ticketId` = `1`

**Query Parameters:**
- `status` = One of: `CHECKED_IN`, `CALLED`, `COMPLETED`, `NO_SHOW`, `FAST_TRACKED`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "status": "IN_CONSULTATION",
    "queueNumber": 1
}
```

---

### 12. Mark Patient as Checked In
**Set status to WAITING**

```
Method: PUT
URL: http://localhost:8080/api/queue/ticket/{ticketId}/checked-in
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "status": "WAITING"
}
```

---

### 13. Mark Patient as No Show
**Patient didn't appear when called**

```
Method: PUT
URL: http://localhost:8080/api/queue/ticket/{ticketId}/no-show
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "status": "NO_SHOW"
}
```

**Note:** This also updates the appointment status to `NO_SHOW`

---

### 14. Mark Patient as Completed
**Consultation finished**

```
Method: PUT
URL: http://localhost:8080/api/queue/ticket/{ticketId}/completed
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "ticketId": 1,
    "status": "COMPLETED"
}
```

**Note:** This also updates the appointment status to `COMPLETED`

---

### 15. Fast-Track Patient
**Give priority to patient (emergency, elderly, etc.)**

```
Method: PUT
URL: http://localhost:8080/api/queue/ticket/{ticketId}/fast-track
```

**Path Variables:**
- `ticketId` = `3`

**Headers:**
```
Content-Type: application/json
```

**Request Body:**
```json
{
    "reason": "Emergency - High fever"
}
```

**Alternative Request Body (uses default reason):**
```json
{}
```

**Sample Response (200 OK):**
```json
{
    "ticketId": 3,
    "status": "FAST_TRACKED",
    "queueNumber": 5,
    "isFastTracked": true,
    "fastTrackReason": "Emergency - High fever"
}
```

**Note:** Patient will be served before non-fast-tracked patients

---

### 16. Process Queue Notifications (Manual Trigger)
**Manually trigger notification processing**

```
Method: POST
URL: http://localhost:8080/api/queue/notify/{doctorId}
```

**Path Variables:**
- `doctorId` = `201`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "message": "Notifications processed successfully"
}
```

**Note:** This is usually called automatically by other endpoints

---

## 🔴 PHASE 4: CLEANUP

### 17. Cancel Queue Ticket (Remove from Queue)
**Patient cancels or leaves - removes them from the active queue**

```
Method: DELETE
URL: http://localhost:8080/api/queue/ticket/{ticketId}
```

**Path Variables:**
- `ticketId` = `1`

**Request Body:** *(None)*

**Sample Response (200 OK):**
```json
{
    "message": "Queue ticket removed successfully - marked as NO_SHOW"
}
```

**Notes:** 
- Queue ticket status is set to `NO_SHOW` (removing from active queue)
- Appointment status is updated to `CANCELLED`
- Remaining patients in queue are automatically notified

---

## 📝 Complete Testing Scenario

### Scenario: Walk-through for 3 Patients

#### Step 1: Check in 3 patients
```
POST /api/queue/check-in/1  → Queue #1 (Patient: John)
POST /api/queue/check-in/2  → Queue #2 (Patient: Jane)
POST /api/queue/check-in/3  → Queue #3 (Patient: Bob)
```

#### Step 2: Fast-track Patient 3 (Bob - Emergency)
```
PUT /api/queue/ticket/3/fast-track
Body: { "reason": "Chest pain - Emergency" }
```

#### Step 3: View queue (Bob should be first due to fast-track)
```
GET /api/queue/doctor/201
```

#### Step 4: Call next patient (should be Bob)
```
POST /api/queue/call-next/201  → Returns Bob (fast-tracked)
```

#### Step 5: Check queue status for John (still waiting)
```
GET /api/queue/status/1
Message: "You are 1 patient(s) away. Please proceed closer..."
```

#### Step 6: Complete Bob's consultation
```
PUT /api/queue/ticket/3/completed
```

#### Step 7: Call next patient (should be John)
```
POST /api/queue/call-next/201  → Returns John
```

#### Step 8: Mark Jane as no-show
```
PUT /api/queue/ticket/2/no-show
```

#### Step 9: View final queue status
```
GET /api/queue/doctor/201  → Empty or only active patients
```

---

## 🎯 Quick Reference - All Endpoints

| # | Method | Endpoint | Purpose |
|---|--------|----------|---------|
| 1 | POST | `/api/queue/check-in/{appointmentId}` | Check in patient |
| 2 | GET | `/api/queue/ticket/{ticketId}` | Get ticket by ID |
| 3 | GET | `/api/queue/appointment/{appointmentId}` | Get ticket by appointment |
| 4 | GET | `/api/queue/status/{ticketId}` | Real-time queue status |
| 5 | GET | `/api/queue/doctor/{doctorId}` | Active queue by doctor |
| 6 | GET | `/api/queue/clinic/{clinicId}` | Active queue by clinic |
| 7 | POST | `/api/queue/call-next/{doctorId}` | Call next patient |
| 16 | GET | `/api/queue/current/{doctorId}` | Current serving number |
| 17 | GET | `/api/queue/count/{doctorId}` | Active queue count |

extra endpoints
| 8 | PUT | `/api/queue/ticket/{ticketId}/status?status={status}` | Update status |
| 9 | PUT | `/api/queue/ticket/{ticketId}/checked-in` | Mark checked in |
| 10 | PUT | `/api/queue/ticket/{ticketId}/no-show` | Mark no-show |
| 11 | PUT | `/api/queue/ticket/{ticketId}/completed` | Mark completed |
| 13 | DELETE | `/api/queue/ticket/{ticketId}` | Cancel ticket |
| 14 | GET | `/api/queue/patient/{patientId}` | Patient queue history |
| 15 | POST | `/api/queue/notify/{doctorId}` | Process notifications |

| 12 | PUT | `/api/queue/ticket/{ticketId}/fast-track` | Fast-track patient |

---

## 🛠️ Postman Collection Setup Tips

### Environment Variables (Recommended)
Create a Postman environment with:
```
base_url = http://localhost:8080
appointment_id = 1
doctor_id = 201
clinic_id = 101
patient_id = 301
ticket_id = 1
```

Then use: `{{base_url}}/api/queue/check-in/{{appointment_id}}`

### Tests to Add (Postman Scripts)

**For Check-In Endpoint:**
```javascript
// Save ticket ID for later use
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

var jsonData = pm.response.json();
pm.environment.set("ticket_id", jsonData.ticketId);
```

**For Queue Status Endpoint:**
```javascript
pm.test("Has queue position", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.positionInQueue).to.be.a('number');
});
```

---

## ⚠️ Common Issues & Solutions

### Issue 1: 404 Not Found
**Cause:** Appointment/ticket doesn't exist
**Solution:** Check database, ensure IDs are correct

### Issue 2: 400 Bad Request - "Already checked in"
**Cause:** Appointment already has a queue ticket
**Solution:** Use different appointment or delete existing ticket

### Issue 3: 400 Bad Request - "No patients in queue"
**Cause:** Trying to call next when queue is empty
**Solution:** Check in patients first

### Issue 4: Status doesn't change
**Cause:** Status transition not allowed
**Solution:** Check current status, follow valid transitions

---

## 📊 Valid Status Transitions

```
CHECKED_IN → CALLED → IN_CONSULTATION → COMPLETED
           ↘                          ↗
            NO_SHOW (also if cancelled)               
         
CHECKED_IN → FAST_TRACKED → CALLED → ...

Note: 
- First patient (queue_number = 1) is automatically set to CALLED upon check-in
- Notifications for "3 away" and "next" are triggered programmatically 
  based on queue position without changing the patient's status.
```

---

## 🎓 Testing Checklist

- [ ] Check in patient successfully
- [ ] View queue status with position
- [ ] View active queue list
- [ ] Fast-track a patient
- [ ] Call next patient (verify fast-track priority)
- [ ] Mark patient as no-show
- [ ] Mark patient as completed
- [ ] Check queue count updates correctly
- [ ] Cancel queue ticket
- [ ] Verify error handling (invalid IDs, duplicate check-ins)

---

**Happy Testing! 🚀**

