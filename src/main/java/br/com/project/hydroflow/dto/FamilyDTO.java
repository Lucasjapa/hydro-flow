package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Family;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record FamilyDTO(
        Long id,

        @NotBlank String name,

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

        @NotNull @Size(min = 1) List<MemberDTO> members) {

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
                family.getMembers().stream().map(MemberDTO::from).toList());
    }
}
