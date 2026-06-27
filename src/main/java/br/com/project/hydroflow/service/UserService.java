package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.UpdateUserDTO;
import br.com.project.hydroflow.dto.UpdateUserRoleDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
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

    public List<UserDTO> findAllUsers() {
        log.info("Listando todos os usuários");
        return userRepository.findAll().stream().map(UserDTO::from).toList();
    }

    public UserDTO saveUser(UserDTO userDTO) {
        log.info("Criando usuário: {}", userDTO.email());

        if (userRepository.existsByEmail(userDTO.email())) {
            log.warn("E-mail já cadastrado: {}", userDTO.email());
            throw new IllegalStateException("E-mail já cadastrado");
        }

        Role role = roleService.findById(userDTO.roleId());
        User user = new User(userDTO.name(), userDTO.email(), passwordEncoder.encode(userDTO.password()), role);
        UserDTO userCreated = UserDTO.from(userRepository.save(user));
        log.info("Usuário criado com sucesso. id: {}", userCreated.id());
        return userCreated;
    }

    public UserDTO updateUser(Long id, UpdateUserDTO updateUserDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Usuário não encontrado para atualização. id: {}", id);
            return new EntityNotFoundException("Usuário não encontrado: " + id);
        });

        user.setName(updateUserDTO.name());
        user.setEmail(updateUserDTO.email());

        UserDTO userUpdated = UserDTO.from(userRepository.save(user));
        log.info("Usuário atualizado com sucesso. id: {}", id);
        return userUpdated;
    }

    public UserDTO updateUserRole(Long id, UpdateUserRoleDTO updateUserRoleDTO) {
        log.info("Atualizando cargo do usuário. id: {}, roleId: {}", id, updateUserRoleDTO.roleId());
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Usuário não encontrado para atualização de cargo. id: {}", id);
            return new EntityNotFoundException("Usuário não encontrado: " + id);
        });

        Role role = roleService.findById(updateUserRoleDTO.roleId());
        user.setRole(role);

        UserDTO userUpdated = UserDTO.from(userRepository.save(user));
        log.info("Cargo do usuário atualizado com sucesso. id: {}", id);
        return userUpdated;
    }

    public void deleteUser(Long id) {
        log.info("Deletando usuário com id: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.warn("Usuário não encontrado para exclusão. id: {}", id);
            return new EntityNotFoundException("Usuário não encontrado: " + id);
        });

        userRepository.delete(user);
        log.info("Usuário deletado com sucesso. id: {}", id);
    }
}
