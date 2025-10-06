package Singheatlh.springboot_backend.mapper;

import Singheatlh.springboot_backend.dto.PatientDTO;
import Singheatlh.springboot_backend.entity.Patient;

public class PatientMapper {
    public static PatientDTO mapToPatientDTO(Patient patient) {
        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail()
        );
    }

    public static Patient mapToPatient(PatientDTO patientDTO) {
        return new Patient(
                patientDTO.getId(),
                patientDTO.getName(),
                patientDTO.getEmail()
        );
    }
}
