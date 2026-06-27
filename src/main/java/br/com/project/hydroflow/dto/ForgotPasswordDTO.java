package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordDTO(
        @NotBlank @Email @Schema(description = "E-mail do usuário", example = "usuario@teste.com")
        String email) {}
