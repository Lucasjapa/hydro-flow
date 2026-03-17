package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.WaterDelivery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record WaterDeliveryDTO(
        Long id,

        @NotNull @Schema(description = "Data da entrega", example = "2024-03-15")
        LocalDate deliveryDate,

        @NotNull @DecimalMin("0.0") @Schema(description = "Quantidade de água em litros", example = "1000.0")
        BigDecimal waterAmountLiters,

        @NotNull @Schema(description = "ID da família", example = "1")
        Long familyId) {

    public static WaterDeliveryDTO from(WaterDelivery waterDelivery) {
        return new WaterDeliveryDTO(
                waterDelivery.getId(),
                waterDelivery.getDeliveryDate(),
                waterDelivery.getWaterAmountLiters(),
                waterDelivery.getFamily().getId());
    }
}
