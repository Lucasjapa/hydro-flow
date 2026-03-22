package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Cistern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CisternDTO(
        Long id,

        @NotNull @DecimalMin("0.0") @Schema(description = "Capacidade total da cisterna em litros", example = "10000.0")
        BigDecimal capacityLiters,

        @Schema(description = "Nível atual da cisterna em litros", example = "5000.0")
        BigDecimal currentLevelLiters) {

    public static CisternDTO from(Cistern cistern) {
        return new CisternDTO(cistern.getId(), cistern.getCapacityLiters(), cistern.getCurrentLevelLiters());
    }
}
