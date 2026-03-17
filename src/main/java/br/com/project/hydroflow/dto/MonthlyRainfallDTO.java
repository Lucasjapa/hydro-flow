package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.MonthlyRainfall;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record MonthlyRainfallDTO(
        Long id,

        @NotNull Integer year,

        @NotNull @Min(1) @Max(12) Integer month,

        @NotNull @DecimalMin("0.0") BigDecimal rainfallMM) {

    public static MonthlyRainfallDTO from(MonthlyRainfall monthlyRainfall) {
        return new MonthlyRainfallDTO(
                monthlyRainfall.getId(),
                monthlyRainfall.getYear(),
                monthlyRainfall.getMonth(),
                monthlyRainfall.getRainfallMM());
    }
}
