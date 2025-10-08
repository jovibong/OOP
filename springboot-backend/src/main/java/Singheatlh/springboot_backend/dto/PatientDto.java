package Singheatlh.springboot_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto extends UserDto {
    private List<AppointmentDto> appointments;
}
