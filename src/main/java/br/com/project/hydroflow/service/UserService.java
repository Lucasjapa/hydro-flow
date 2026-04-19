package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Criando usuário: {}", userDTO.email());
        Role role = roleService.findById(userDTO.roleId());
        User user = new User(userDTO.name(), userDTO.email(), passwordEncoder.encode(userDTO.password()), role);
        UserDTO userCreated = UserDTO.from(userRepository.save(user));
        log.info("Usuário criado com sucesso. id: {}", userCreated.id());
        return userCreated;
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        log.info("Atualizando usuário com id: {}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Usuário não encontrado para atualização. id: {}", id);
            return new EntityNotFoundException("Usuário não encontrado: " + id);
        });

        user.setName(userDTO.name());
        user.setEmail(userDTO.email());

        if (userDTO.password() != null && !userDTO.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        if (userDTO.roleId() != null) {
            user.setRole(roleService.findById(userDTO.roleId()));
        }

        UserDTO userUpdated = UserDTO.from(userRepository.save(user));
        log.info("Usuário atualizado com sucesso. id: {}", id);
        return userUpdated;
    }
}
