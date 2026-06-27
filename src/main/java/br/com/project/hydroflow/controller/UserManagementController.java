package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.dto.UpdateUserRoleDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.security.annotation.AdminOnly;
import br.com.project.hydroflow.security.annotation.AdminOrManageUsers;
import br.com.project.hydroflow.service.PermissionService;
import br.com.project.hydroflow.service.RoleService;
import br.com.project.hydroflow.service.UserService;
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
@RequestMapping("/hf/user-management")
@AdminOnly
@Tag(name = "Gerenciamento de Usuários", description = "Operações administrativas de usuários")
@ApiResponses({
    @ApiResponse(responseCode = "401", description = "Não autenticado"),
    @ApiResponse(responseCode = "403", description = "Acesso negado"),
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
public class UserManagementController {

    private final UserService userService;
    private final RoleService roleService;
    private final PermissionService permissionService;

    public UserManagementController(
            UserService userService, RoleService roleService, PermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping
    @AdminOrManageUsers
    @Operation(summary = "Lista todos os usuários")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")})
    public ResponseEntity<List<UserDTO>> findAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @PutMapping("/users/{id}/role")
    @AdminOrManageUsers
    @Operation(summary = "Atualiza o cargo de um usuário")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "403", description = "Sem permissão para atualizar cargo"),
        @ApiResponse(responseCode = "404", description = "Usuário ou cargo não encontrado"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<UserDTO> updateUserRole(
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id,
            @RequestBody @Valid UpdateUserRoleDTO updateUserRoleDTO) {
        return ResponseEntity.ok(userService.updateUserRole(id, updateUserRoleDTO));
    }

    @GetMapping("/roles")
    @AdminOrManageUsers
    @Operation(summary = "Lista todos os cargos")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")})
    public ResponseEntity<List<RoleDTO>> findAllRoles() {
        return ResponseEntity.ok(roleService.findAllRoles());
    }

    @GetMapping("/permissions")
    @AdminOrManageUsers
    @Operation(summary = "Lista todas as permissões")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")})
    public ResponseEntity<List<PermissionDTO>> findAllPermissions() {
        return ResponseEntity.ok(permissionService.findAllPermissions());
    }

    @PostMapping("/create-role")
    @AdminOnly
    @Operation(summary = "Cria um novo cargo")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<RoleDTO> createRole(@RequestBody @Valid RoleDTO roleDTO) {
        RoleDTO roleCreated = roleService.saveRole(roleDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(roleCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(roleCreated);
    }

    @PutMapping("/roles/{id}")
    @AdminOnly
    @Operation(summary = "Atualiza um cargo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cargo atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
        @ApiResponse(responseCode = "422", description = "Erro de regra de negócio")
    })
    public ResponseEntity<RoleDTO> updateRole(
            @Parameter(description = "ID do cargo", example = "1") @PathVariable Long id,
            @RequestBody @Valid RoleDTO roleDTO) {
        return ResponseEntity.ok(roleService.updateRole(id, roleDTO));
    }

    @DeleteMapping("/roles/{id}")
    @AdminOnly
    @Operation(summary = "Deleta um cargo")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Cargo deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cargo não encontrado"),
        @ApiResponse(responseCode = "422", description = "Cargo possui usuários vinculados")
    })
    public ResponseEntity<Void> deleteRole(
            @Parameter(description = "ID do cargo", example = "1") @PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}
