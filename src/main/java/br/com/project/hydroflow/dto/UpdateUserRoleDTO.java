package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleDTO(
        @NotNull @Schema(description = "ID do novo cargo do usuário", example = "1")
        Long roleId) {}
