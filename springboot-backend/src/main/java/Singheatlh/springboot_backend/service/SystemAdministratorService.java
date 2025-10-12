package Singheatlh.springboot_backend.service;

import Singheatlh.springboot_backend.dto.SystemAdministratorDto;

import java.util.List;

public interface SystemAdministratorService {
    SystemAdministratorDto getById(String id);
    SystemAdministratorDto createSystemAdministrator(SystemAdministratorDto adminDto);
    List<SystemAdministratorDto> getAllSystemAdministrators();
    SystemAdministratorDto updateSystemAdministrator(SystemAdministratorDto adminDto);
    void deleteSystemAdministrator(String id);
}
