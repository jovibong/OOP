package Singheatlh.springboot_backend.entity;

import Singheatlh.springboot_backend.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Patient")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    @Column(unique = true)
    String email;

    @Column(name = "hashed_password")
    String hashedPassword;

    public Patient(Long id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
