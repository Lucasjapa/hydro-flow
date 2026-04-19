package br.com.project.hydroflow.dto;

import br.com.project.hydroflow.domain.Role;

public record RoleDTO(Long id, String name) {
    public static RoleDTO from(Role role) {
        return new RoleDTO(role.getId(), role.getName());
    }
}
