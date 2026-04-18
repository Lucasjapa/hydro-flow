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

        @NotNull Boolean hasGutterSystem,

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

        @Schema(description = "Status da família", example = "NORMAL")
        Family.FamilyStatus familyStatus,

        @NotNull @Size(min = 1) List<MemberDTO> members,

        @NotNull @Size(min = 1) List<CisternDTO> cisterns,

        @Schema(description = "Consumo diário total da família em litros", example = "56.0")
        BigDecimal dailyConsumption,

        @Schema(description = "Quantidade de dias restantes até esvaziar a cisterna", example = "30")
        Integer remainingDays,

        @Schema(description = "Previsão da próxima data de entrega", example = "2024-04-15")
        LocalDate nextDeliveryDate) {

    public static FamilyDTO from(Family family) {
        return new FamilyDTO(
                family.getId(),
                family.getName(),
                family.isHasGutterSystem(),
                family.getLatitude(),
                family.getLongitude(),
                family.getFamilyStatus(),
                family.getMembers().stream().map(MemberDTO::from).toList(),
                family.getCisterns().stream().map(CisternDTO::from).toList(),
                null,
                null,
                null);
    }

    public static FamilyDTO from(
            Family family, BigDecimal dailyConsumption, Integer remainingDays, LocalDate nextDeliveryDate) {

        return new FamilyDTO(
                family.getId(),
                family.getName(),
                family.isHasGutterSystem(),
                family.getLatitude(),
                family.getLongitude(),
                family.getFamilyStatus(),
                family.getMembers().stream().map(MemberDTO::from).toList(),
                family.getCisterns().stream().map(CisternDTO::from).toList(),
                dailyConsumption,
                remainingDays,
                nextDeliveryDate);
    }
}
