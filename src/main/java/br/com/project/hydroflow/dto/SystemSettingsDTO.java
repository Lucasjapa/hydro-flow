package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.SystemSettings;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record SystemSettingsDTO(
        Long id,
        @NotNull @DecimalMin("0.0") BigDecimal dailyWaterConsumption,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal gutterEfficiencyCoefficient) {
    public static SystemSettingsDTO from(SystemSettings systemSettings) {
        return new SystemSettingsDTO(
                systemSettings.getId(),
                systemSettings.getDailyWaterConsumption(),
                systemSettings.getGutterEfficiencyCoefficient());
    }
}
