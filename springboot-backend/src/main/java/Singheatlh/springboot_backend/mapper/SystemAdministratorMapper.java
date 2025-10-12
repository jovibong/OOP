package Singheatlh.springboot_backend.mapper;

import Singheatlh.springboot_backend.dto.SystemAdministratorDto;
import Singheatlh.springboot_backend.entity.SystemAdministrator;
import org.springframework.stereotype.Component;

@Component
public class SystemAdministratorMapper {
    public SystemAdministratorDto toDto(SystemAdministrator systemAdministrator) {
        if  (systemAdministrator == null) {
            return null;
        }

        SystemAdministratorDto systemAdministratorDto = new SystemAdministratorDto();
        systemAdministratorDto.setId(systemAdministrator.getSupabaseUid());
        systemAdministratorDto.setUsername(systemAdministrator.getUsername());
        systemAdministratorDto.setName(systemAdministrator.getName());
        systemAdministratorDto.setEmail(systemAdministrator.getEmail());
        systemAdministratorDto.setRole(systemAdministrator.getRole());
        return systemAdministratorDto;
    }

    public SystemAdministrator toEntity(SystemAdministratorDto systemAdministratorDto) {
        if (systemAdministratorDto == null) {
            return null;
        }
        SystemAdministrator systemAdministrator = new SystemAdministrator();
        systemAdministrator.setSupabaseUid(systemAdministratorDto.getId());
        systemAdministrator.setUsername(systemAdministratorDto.getUsername());
        systemAdministrator.setName(systemAdministratorDto.getName());
        systemAdministrator.setEmail(systemAdministratorDto.getEmail());
        systemAdministrator.setRole(systemAdministratorDto.getRole());
        return systemAdministrator;
    }
}
