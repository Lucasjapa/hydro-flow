package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(
        Long id,

        @NotBlank @Schema(description = "Nome do Usuário", example = "joao teste")
        String name,

        @NotBlank @Email String email,

        @NotBlank @Size(min = 8) @Schema(description = "Senha do usuário (mínimo 8 caracteres)", example = "senha123")
        String password,

        @NotNull @Schema(description = "ID do cargo do usuário", example = "1")
        Long roleId) {

    public static UserDTO from(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                null,
                user.getRole() != null ? user.getRole().getId() : null);
    }
}
