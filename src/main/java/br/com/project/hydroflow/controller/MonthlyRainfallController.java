package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.dto.MonthlyRainfallDTO;
import br.com.project.hydroflow.service.MonthlyRainfallService;
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
@RequestMapping("/hf/monthly-rainfall")
@Tag(name = "Chuva Mensal", description = "Gerenciamento de registros de chuva mensal")
@ApiResponses({
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Acesso negado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
public class MonthlyRainfallController {

    private final MonthlyRainfallService monthlyRainfallService;

    public MonthlyRainfallController(MonthlyRainfallService monthlyRainfallService) {
        this.monthlyRainfallService = monthlyRainfallService;
    }

    @PostMapping
    @Operation(summary = "Cria um novo registro de chuva mensal")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Registro criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<MonthlyRainfallDTO> save(@RequestBody @Valid MonthlyRainfallDTO dto) {
        MonthlyRainfallDTO monthlyRainfallCreated = monthlyRainfallService.saveMonthlyRainfall(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(monthlyRainfallCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(monthlyRainfallCreated);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um registro de chuva mensal")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<MonthlyRainfallDTO> update(
            @PathVariable Long id, @RequestBody @Valid MonthlyRainfallDTO dto) {
        MonthlyRainfallDTO updated = monthlyRainfallService.updateMonthlyRainfall(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um registro de chuva mensal")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro removido com sucesso"),
        @ApiResponse(responseCode = "404", description = "Registro não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        monthlyRainfallService.deleteMonthlyRainfall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/year/{year}")
    @Operation(summary = "Busca todos os registros de chuva mensal por ano")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registros encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Nenhum registro encontrado")
    })
    public ResponseEntity<List<MonthlyRainfallDTO>> findByYear(
            @Parameter(description = "Ano do registro", example = "2024") @PathVariable Integer year) {
        return ResponseEntity.ok(monthlyRainfallService.findByYear(year));
    }
}
