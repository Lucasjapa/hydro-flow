package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Family;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record FamilyDTO(
        Long id,

        @NotBlank @Schema(description = "Nome da Família", example = "Família Teste")
        String name,

        @NotNull @DecimalMin("0.0") @Schema(description = "Capacidade total da cisterna em litros", example = "10000.0")
        BigDecimal cisternCapacityLiters,

        @NotNull @DecimalMin("0.0") @Schema(description = "Nível atual da cisterna em litros", example = "5000.0")
        BigDecimal cisternCurrentLevelLiters,

        @NotNull Boolean hasGutterSystem,

        @Schema(description = "Área de captação da calha em metros quadrados", example = "50.0")
        BigDecimal gutterAreaM2,

        @Schema(description = "Coeficiente de eficiência da calha (0 a 1)", example = "0.80")
        BigDecimal gutterEfficiencyCoefficient,

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        @Schema(description = "Latitude geográfica", example = "-8.123456")
        BigDecimal latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        @Schema(description = "Longitude geográfica", example = "-36.123456")
        BigDecimal longitude,

        @NotNull @Size(min = 1) List<MemberDTO> members,

        @Schema(description = "Consumo diário total da família em litros", example = "56.0")
        BigDecimal dailyConsumption,

        @Schema(description = "Quantidade de dias restantes até esvaziar a cisterna", example = "30")
        Integer remainingDays,

        @Schema(description = "Previsão da próxima data de entrega", example = "2024-04-15")
        LocalDate nextDeliveryDate,

        @Schema(description = "Status do reservatório", example = "LOW")
        Family.CisternStatus cisternStatus) {

    public static FamilyDTO from(Family family) {
        return new FamilyDTO(
                family.getId(),
                family.getName(),
                family.getCisternCapacityLiters(),
                family.getCisternCurrentLevelLiters(),
                family.isHasGutterSystem(),
                family.getGutterAreaM2(),
                family.getGutterEfficiencyCoefficient(),
                family.getLatitude(),
                family.getLongitude(),
                family.getMembers().stream().map(MemberDTO::from).toList(),
                null,
                null,
                null,
                family.getCisternStatus());
    }

    public static FamilyDTO from(
            Family family,
            BigDecimal dailyConsumption,
            Integer remainingDays,
            LocalDate nextDeliveryDate,
            Family.CisternStatus cisternStatus) {

        return new FamilyDTO(
                family.getId(),
                family.getName(),
                family.getCisternCapacityLiters(),
                family.getCisternCurrentLevelLiters(),
                family.isHasGutterSystem(),
                family.getGutterAreaM2(),
                family.getGutterEfficiencyCoefficient(),
                family.getLatitude(),
                family.getLongitude(),
                family.getMembers().stream().map(MemberDTO::from).toList(),
                dailyConsumption,
                remainingDays,
                nextDeliveryDate,
                cisternStatus);
    }
}
