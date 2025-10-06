package Singheatlh.springboot_backend.service;

import Singheatlh.springboot_backend.dto.PatientDTO;

import java.util.List;

public interface PatientService {
    PatientDTO getById(Long id);
    PatientDTO createPatient(PatientDTO patientDTO);
    List<PatientDTO> getAllPatients();
    PatientDTO updatePatient(PatientDTO patientDTO);
    void deletePatient(Long id);
}
