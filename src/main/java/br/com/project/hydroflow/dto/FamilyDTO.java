package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Family;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public record FamilyDTO(
        Long id,

        @NotBlank String name,

        @NotNull @DecimalMin("0.0") BigDecimal cisternCapacityLiters,

        @NotNull Boolean hasGutterSystem,

        BigDecimal gutterAreaM2,

        BigDecimal gutterEfficiencyCoefficient,

        @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal latitude,

        @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal longitude,

        @NotNull @Size(min = 1) List<MemberDTO> members) {

    public static FamilyDTO from(Family family) {
        return new FamilyDTO(
                family.getId(),
                family.getName(),
                family.getCisternCapacityLiters(),
                family.isHasGutterSystem(),
                family.getGutterAreaM2(),
                family.getGutterEfficiencyCoefficient(),
                family.getLatitude(),
                family.getLongitude(),
                family.getMembers().stream().map(MemberDTO::from).toList());
    }
}
