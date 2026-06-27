package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Permission;
import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.repository.PermissionRepository;
import br.com.project.hydroflow.repository.RoleRepository;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.Permissions;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public RoleService(
            RoleRepository roleRepository, PermissionRepository permissionRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
    }

    public List<RoleDTO> findAllRoles() {
        log.info("Listando todos os cargos");
        return roleRepository.findAll().stream().map(RoleDTO::from).toList();
    }

    public Role findById(Long id) {
        log.info("Buscando cargo com id: {}", id);
        return roleRepository.findById(id).orElseThrow(() -> {
            log.warn("Cargo não encontrado. id: {}", id);
            return new EntityNotFoundException("Cargo não encontrado: " + id);
        });
    }

    public RoleDTO saveRole(RoleDTO roleDTO) {
        log.info("Criando cargo: {}", roleDTO.name());
        Role role = new Role(roleDTO.name());

        if (roleDTO.permissions() != null && !roleDTO.permissions().isEmpty()) {
            role.getPermissions().addAll(resolvePermissions(roleDTO.permissions()));
        }

        RoleDTO roleCreated = RoleDTO.from(roleRepository.save(role));
        log.info("Cargo criado com sucesso. id: {}", roleCreated.id());
        return roleCreated;
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        log.info("Atualizando cargo com id: {}", id);
        Role role = findById(id);
        role.setName(roleDTO.name());

        if (roleDTO.permissions() != null) {
            boolean hadAdmin = role.getPermissions().stream()
                    .anyMatch(permission -> Permissions.ADMIN.equals(permission.getName()));
            Set<Permission> newPermissions =
                    roleDTO.permissions().isEmpty() ? Set.of() : resolvePermissions(roleDTO.permissions());
            boolean willHaveAdmin = newPermissions.stream()
                    .anyMatch(permission -> Permissions.ADMIN.equals(permission.getName()));

            if (hadAdmin && !willHaveAdmin) {
                validateNotLastAdminRole(role.getId());
            }

            role.getPermissions().clear();
            role.getPermissions().addAll(newPermissions);
        }

        RoleDTO roleUpdated = RoleDTO.from(roleRepository.save(role));
        log.info("Cargo atualizado com sucesso. id: {}", id);
        return roleUpdated;
    }

    public void deleteRole(Long id) {
        log.info("Deletando cargo com id: {}", id);
        Role role = findById(id);

        if (userRepository.existsByRole_Id(id)) {
            log.warn("Não é possível deletar cargo com usuários vinculados. id: {}", id);
            throw new IllegalStateException("Não é possível deletar cargo com usuários vinculados");
        }

        roleRepository.delete(role);
        log.info("Cargo deletado com sucesso. id: {}", id);
    }

    private void validateNotLastAdminRole(Long roleId) {
        if (roleRepository.countByPermissions_Name(Permissions.ADMIN) <= 1) {
            log.warn("Não é possível remover permissão ADMIN do único cargo que a possui. id: {}", roleId);
            throw new IllegalStateException("Não é possível remover a permissão Administrador do único cargo que a possui");
        }
    }

    private Set<Permission> resolvePermissions(List<PermissionDTO> permissionDTOs) {
        List<Long> permissionIds = permissionDTOs.stream()
                .map(PermissionDTO::id)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (permissionIds.isEmpty()) {
            return Set.of();
        }

        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        if (permissions.size() != permissionIds.size()) {
            log.warn("Permissão não encontrada ao vincular ao cargo. ids: {}", permissionIds);
            throw new EntityNotFoundException("Permissão não encontrada");
        }

        return new HashSet<>(permissions);
    }
}
