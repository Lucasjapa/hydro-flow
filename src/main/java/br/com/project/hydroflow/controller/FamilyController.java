package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/hf/family")
@Tag(name = "Family", description = "Gerenciamento de famílias")
@ApiResponses({
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Acesso negado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
public class FamilyController {

    private final FamilyService familyService;

    public FamilyController(FamilyService familyService) {
        this.familyService = familyService;
    }

    @PostMapping
    @Operation(summary = "Cria uma nova família")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Família criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<FamilyDTO> createFamily(@RequestBody @Valid FamilyDTO familyDTO) {
        FamilyDTO created = familyService.createFamily(familyDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.id())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma família")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Família atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Família não encontrada"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable Long id, @RequestBody @Valid FamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.updateFamily(id, familyDTO));
    }
}
