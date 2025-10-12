package Singheatlh.springboot_backend.controller;

import Singheatlh.springboot_backend.dto.SystemAdministratorDto;
import Singheatlh.springboot_backend.dto.request.CreateSystemAdministratorRequest;
import Singheatlh.springboot_backend.service.SystemAdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/system-administrators")
@RequiredArgsConstructor
public class SystemAdministratorController {
    private final SystemAdministratorService systemAdministratorService;

    @PostMapping
    public ResponseEntity<SystemAdministratorDto> createSystemAdministrator(
            @RequestBody CreateSystemAdministratorRequest createRequest) {
        SystemAdministratorDto adminDto = SystemAdministratorDto.builder()
                .id(createRequest.getId())
                .username(createRequest.getUsername())
                .name(createRequest.getName())
                .email(createRequest.getEmail())
                .build();

        SystemAdministratorDto createdAdmin = systemAdministratorService.createSystemAdministrator(adminDto);
        return new ResponseEntity<>(createdAdmin, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SystemAdministratorDto> getSystemAdministratorById(@PathVariable String id) {
        SystemAdministratorDto admin = systemAdministratorService.getById(id);
        return ResponseEntity.ok(admin);
    }

    @GetMapping
    public ResponseEntity<List<SystemAdministratorDto>> getAllSystemAdministrators() {
        List<SystemAdministratorDto> admins = systemAdministratorService.getAllSystemAdministrators();
        return ResponseEntity.ok(admins);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SystemAdministratorDto> updateSystemAdministrator(
            @PathVariable String id,
            @RequestBody SystemAdministratorDto adminDto) {
        adminDto.setId(id); // Ensure path ID is used
        SystemAdministratorDto updatedAdmin = systemAdministratorService.updateSystemAdministrator(adminDto);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSystemAdministrator(@PathVariable String id) {
        systemAdministratorService.deleteSystemAdministrator(id);
        return ResponseEntity.ok("System Administrator deleted successfully!");
    }
}
