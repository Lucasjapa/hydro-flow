package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberDTO(
        Long id,

        @NotBlank String name,

        @NotNull @Min(0) @Max(150) @Schema(description = "Idade do membro", example = "35")
        Integer age,

        @NotNull Boolean isBedridden) {

    public static MemberDTO from(Member member) {
        return new MemberDTO(member.getId(), member.getName(), member.getAge(), member.isBedridden());
    }
}
