package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.security.annotation.AdminOrManageUsers;
import br.com.project.hydroflow.security.annotation.AuthenticatedOnly;
import br.com.project.hydroflow.service.FamilyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/hf/families")
@AuthenticatedOnly
@Tag(name = "Família", description = "Gerenciamento de famílias")
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
    @AdminOrManageUsers
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Família criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<FamilyDTO> createFamily(@RequestBody @Valid FamilyDTO familyDTO) {
        FamilyDTO familyCreated = familyService.saveFamily(familyDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(familyCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(familyCreated);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma família")
    @AdminOrManageUsers
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Família atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Família não encontrada"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<FamilyDTO> updateFamily(@PathVariable Long id, @RequestBody @Valid FamilyDTO familyDTO) {
        return ResponseEntity.ok(familyService.updateFamily(id, familyDTO));
    }

    @Operation(summary = "Buscar família por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Família encontrada"),
        @ApiResponse(responseCode = "404", description = "Família não encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FamilyDTO> findFamilyById(
            @Parameter(description = "ID da família", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(familyService.findFamilyById(id));
    }

    @GetMapping
    @Operation(summary = "Listar famílias", description = "Lista todas as famílias ou filtra por nome ou status")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")})
    public ResponseEntity<Page<FamilyDTO>> findAllFamilies(
            @Parameter(description = "Filtrar pelo nome da família", example = "Silva") @RequestParam(required = false)
                    String name,
            @Parameter(description = "Filtrar pelo status da família", example = "URGENT")
                    @RequestParam(required = false)
                    Family.FamilyStatus status,
            @Parameter(description = "Ordenação por nível da cisterna", example = "asc") @RequestParam(required = false)
                    String cisternLevelSort,
            @Parameter(description = "Número da página", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade por página", example = "10") @RequestParam(defaultValue = "10")
                    int size,
            @Parameter(description = "Ordenação por nome", example = "asc") @RequestParam(required = false)
                    String nameSort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        if (status != null) {
            return ResponseEntity.ok(familyService.findFamiliesByStatus(status, pageable));
        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(familyService.findFamiliesByName(name, pageable));
        }
        if ("asc".equalsIgnoreCase(nameSort)) {
            pageable = PageRequest.of(page, size, Sort.by("name").ascending());
            return ResponseEntity.ok(familyService.findAllFamilies(pageable));
        }
        if ("desc".equalsIgnoreCase(nameSort)) {
            pageable = PageRequest.of(page, size, Sort.by("name").descending());
            return ResponseEntity.ok(familyService.findAllFamilies(pageable));
        }
        if ("asc".equalsIgnoreCase(cisternLevelSort)) {
            return ResponseEntity.ok(familyService.findAllOrderByCisternLevelAsc(pageable));
        }
        if ("desc".equalsIgnoreCase(cisternLevelSort)) {
            return ResponseEntity.ok(familyService.findAllOrderByCisternLevelDesc(pageable));
        }
        return ResponseEntity.ok(familyService.findAllFamilies(pageable));
    }
}
