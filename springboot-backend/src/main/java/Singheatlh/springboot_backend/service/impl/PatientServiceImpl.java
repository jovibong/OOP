package Singheatlh.springboot_backend.service.impl;

import Singheatlh.springboot_backend.dto.PatientDTO;
import Singheatlh.springboot_backend.entity.Patient;
import Singheatlh.springboot_backend.exception.ResourceNotFoundExecption;
import Singheatlh.springboot_backend.mapper.PatientMapper;
import Singheatlh.springboot_backend.repository.PatientRepository;
import Singheatlh.springboot_backend.service.PatientService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {

    private PatientRepository patientRepository;

    @Override
    public PatientDTO getById(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundExecption("Patient does not exist with the given id " + id)
        );
        return PatientMapper.mapToPatientDTO(patient);
    }

    @Override
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = PatientMapper.mapToPatient(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.mapToPatientDTO(savedPatient);
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::mapToPatientDTO).collect(Collectors.toList());
    }

    @Override
    public PatientDTO updatePatient(PatientDTO patientDTO) {
        Patient patient = patientRepository.findById(patientDTO.getId()).orElseThrow(
                ()->new ResourceNotFoundExecption("Patient does not exist with the given id " + patientDTO)
        );
        patient.setName(patientDTO.getName());
        patient.setEmail(patientDTO.getEmail());
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.mapToPatientDTO(savedPatient);

    }

    @Override
    public void deletePatient(Long id) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundExecption("Patient does not exist with the given id " + id)
        );
        patientRepository.deleteById(id);

    }
}
