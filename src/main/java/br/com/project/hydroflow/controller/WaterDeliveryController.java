package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.security.annotation.AdminOrRegisterDelivery;
import br.com.project.hydroflow.service.WaterDeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/hf/water-deliveries")
@Tag(name = "Entrega de Água", description = "Gerenciamento de entregas de água")
@ApiResponses({
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Acesso negado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
public class WaterDeliveryController {

    private final WaterDeliveryService waterDeliveryService;

    public WaterDeliveryController(WaterDeliveryService waterDeliveryService) {
        this.waterDeliveryService = waterDeliveryService;
    }

    @PostMapping
    @Operation(summary = "Cria uma nova entrega de água")
    @AdminOrRegisterDelivery
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Entrega criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<WaterDeliveryDTO> save(@RequestBody @Valid WaterDeliveryDTO dto) {
        WaterDeliveryDTO created = waterDeliveryService.saveWaterDelivery(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/year/{year}/family/{familyId}")
    @Operation(summary = "Busca entregas de água por ano e família")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Entregas encontradas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhuma entrega encontrada")
    })
    public ResponseEntity<List<WaterDeliveryDTO>> findByYearAndFamilyId(
            @Parameter(description = "Ano da entrega", example = "2024") @PathVariable Integer year,
            @Parameter(description = "ID da família", example = "1") @PathVariable Long familyId) {
        return ResponseEntity.ok(waterDeliveryService.findByYearAndFamilyId(year, familyId));
    }
}
