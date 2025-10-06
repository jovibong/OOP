package Singheatlh.springboot_backend.controller;

import Singheatlh.springboot_backend.dto.PatientDTO;
import Singheatlh.springboot_backend.entity.Patient;
import Singheatlh.springboot_backend.service.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@AllArgsConstructor
@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private PatientService patientService;

    @GetMapping("{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable("id") long patientId) {
        PatientDTO patientDTO = patientService.getById(patientId);
        return ResponseEntity.ok(patientDTO);
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<PatientDTO> patients =  patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO newPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(newPatient,HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PatientDTO> updatePatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO newPatient = patientService.updatePatient(patientDTO);
        return ResponseEntity.ok(newPatient);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePatient(@PathVariable("id") Long  patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok("Patient Deleted successfully!");
    }



}
