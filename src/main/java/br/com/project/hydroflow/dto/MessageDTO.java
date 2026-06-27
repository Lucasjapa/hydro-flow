package br.com.project.hydroflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record MessageDTO(
        @Schema(description = "Mensagem de retorno", example = "Operação realizada com sucesso")
        String message) {}
