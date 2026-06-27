package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordDTO(
        @NotBlank
//        @Pattern(regexp = "\\d{6}", message = "Token inválido")
        @Schema(description = "Token de recuperação enviado por e-mail", example = "123456")
        String token,

        @NotBlank @Size(min = 8) @Schema(description = "Nova senha do usuário", example = "novaSenha123")
        String newPassword) {}
