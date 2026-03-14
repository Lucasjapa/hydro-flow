package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.dto.SystemSettingsDTO;
import br.com.project.hydroflow.service.SystemSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system-settings")
@Tag(name = "System Settings", description = "Gerenciamento de configurações do sistema")
public class SystemSettingsController {

    private final SystemSettingsService systemSettingsService;

    public SystemSettingsController(SystemSettingsService systemSettingsService) {
        this.systemSettingsService = systemSettingsService;
    }

    @GetMapping
    @Operation(summary = "Busca as configurações do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configurações encontradas"),
        @ApiResponse(responseCode = "404", description = "Configurações não encontradas")
    })
    public ResponseEntity<SystemSettingsDTO> findSystemSettings() {
        return ResponseEntity.ok(systemSettingsService.findSystemSettings());
    }

    @PutMapping
    @Operation(summary = "Atualiza as configurações do sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Configurações atualizadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Configurações não encontradas"),
        @ApiResponse(responseCode = "422", description = "Dados inválidos")
    })
    public ResponseEntity<SystemSettingsDTO> updateSystemSettings(@RequestBody @Valid SystemSettingsDTO systemSettingsDTO) {
        return ResponseEntity.ok(systemSettingsService.updateSystemSettings(systemSettingsDTO));
    }
}
