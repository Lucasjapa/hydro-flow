package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.MonthlyRainfall;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MonthlyRainfallDTO(
        Long id,

        @NotNull @Schema(description = "Ano do registro", example = "2024")
        Integer year,

        @NotNull @Min(1) @Max(12) @Schema(description = "Mês do registro (1-12)", example = "3")
        Integer month,

        @NotNull @DecimalMin("0.0") @Schema(description = "Precipitação em milímetros", example = "120.5")
        BigDecimal rainfallMM) {

    public static MonthlyRainfallDTO from(MonthlyRainfall monthlyRainfall) {
        return new MonthlyRainfallDTO(
                monthlyRainfall.getId(),
                monthlyRainfall.getYear(),
                monthlyRainfall.getMonth(),
                monthlyRainfall.getRainfallMM());
    }
}
