package Singheatlh.springboot_backend.repository;

import Singheatlh.springboot_backend.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient,Long> {
}
