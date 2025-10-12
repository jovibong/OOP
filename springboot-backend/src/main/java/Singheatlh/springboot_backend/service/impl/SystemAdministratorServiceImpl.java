package Singheatlh.springboot_backend.service.impl;

import Singheatlh.springboot_backend.dto.SystemAdministratorDto;
import Singheatlh.springboot_backend.entity.SystemAdministrator;
import Singheatlh.springboot_backend.entity.enums.Role;
import Singheatlh.springboot_backend.exception.ResourceNotFoundExecption;
import Singheatlh.springboot_backend.mapper.SystemAdministratorMapper;
import Singheatlh.springboot_backend.repository.SystemAdministratorRepository;
import Singheatlh.springboot_backend.service.SystemAdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SystemAdministratorServiceImpl implements SystemAdministratorService {
    private final SystemAdministratorRepository systemAdministratorRepository;
    private final SystemAdministratorMapper systemAdministratorMapper;
    @Override
    public SystemAdministratorDto getById(String id) {
        SystemAdministrator admin = systemAdministratorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundExecption("System Administrator not found with id: " + id));
        return systemAdministratorMapper.toDto(admin);
    }

    @Override
    public SystemAdministratorDto createSystemAdministrator(SystemAdministratorDto adminDto) {
        if (systemAdministratorRepository.existsById(adminDto.getId())) {
            throw new RuntimeException("System Administrator already exists with id: " + adminDto.getId());
        }

        SystemAdministrator admin = systemAdministratorMapper.toEntity(adminDto);
        admin.setRole(Role.SYSTEM_ADMINISTRATOR);

        SystemAdministrator savedAdmin = systemAdministratorRepository.save(admin);
        return systemAdministratorMapper.toDto(savedAdmin);
    }

    @Override
    public List<SystemAdministratorDto> getAllSystemAdministrators() {
        List<SystemAdministrator> admins = systemAdministratorRepository.findAll();
        return admins.stream()
                .map(systemAdministratorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SystemAdministratorDto updateSystemAdministrator(SystemAdministratorDto adminDto) {
        SystemAdministrator admin = systemAdministratorRepository.findById(adminDto.getId())
                .orElseThrow(() -> new ResourceNotFoundExecption("System Administrator not found with id: " + adminDto.getId()));

        // Update only allowed fields
        admin.setName(adminDto.getName());
        admin.setUsername(adminDto.getUsername());
        // Email and role should be updated through separate auth service

        SystemAdministrator savedAdmin = systemAdministratorRepository.save(admin);
        return systemAdministratorMapper.toDto(savedAdmin);
    }

    @Override
    public void deleteSystemAdministrator(String id) {
        if (!systemAdministratorRepository.existsById(id)) {
            throw new ResourceNotFoundExecption("System Administrator not found with id: " + id);
        }
        systemAdministratorRepository.deleteById(id);
    }
}
