package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleService.class);

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
        RoleDTO roleCreated = RoleDTO.from(roleRepository.save(role));
        log.info("Cargo criado com sucesso. id: {}", roleCreated.id());
        return roleCreated;
    }

    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        log.info("Atualizando cargo com id: {}", id);
        Role role = findById(id);
        role.setName(roleDTO.name());
        RoleDTO roleUpdated = RoleDTO.from(roleRepository.save(role));
        log.info("Cargo atualizado com sucesso. id: {}", id);
        return roleUpdated;
    }

    public void deleteRole(Long id) {
        log.info("Deletando cargo com id: {}", id);
        Role role = findById(id);
        roleRepository.delete(role);
        log.info("Cargo deletado com sucesso. id: {}", id);
    }
}
