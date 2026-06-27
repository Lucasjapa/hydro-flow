package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.UpdateUserDTO;
import br.com.project.hydroflow.dto.UpdateUserRoleDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private Role defaultRole;
    private User defaultUser;
    private User secondUser;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, roleService, passwordEncoder);

        defaultRole = new Role("USER");
        defaultRole.setId(1L);

        this.defaultUser = User.builder()
                .id(1L)
                .name("Maria")
                .email("maria@example.com")
                .password("hash1")
                .role(defaultRole)
                .build();

        this.secondUser = User.builder()
                .id(2L)
                .name("João")
                .email("joao@example.com")
                .password("hash2")
                .role(defaultRole)
                .build();
    }

    @Nested
    @DisplayName("findAllUsers")
    class FindAllUsers {

        @Test
        @DisplayName("deve listar todos os usuários")
        void testFindAllUsers() {
            when(userRepository.findAll()).thenReturn(List.of(defaultUser, secondUser));

            List<UserDTO> result = userService.findAllUsers();

            assertThat(result).hasSize(2);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().name()).isEqualTo("Maria");
            assertThat(result.getFirst().password()).isNull();
            assertThat(result.getLast().id()).isEqualTo(2L);
            assertThat(result.getLast().email()).isEqualTo("joao@example.com");
            assertThat(result.getLast().password()).isNull();
        }
    }

    @Nested
    @DisplayName("saveUser")
    class SaveUser {

        @Test
        @DisplayName("deve persistir usuário")
        void testSaveUser() {
            UserDTO input = new UserDTO(null, "Maria", "maria@example.com", "senha1234", 1L);
            when(roleService.findById(1L)).thenReturn(defaultRole);
            when(passwordEncoder.encode("senha1234")).thenReturn("hash-senha");

            User persisted = User.builder()
                    .id(10L)
                    .name("Maria")
                    .email("maria@example.com")
                    .password("hash-senha")
                    .role(defaultRole)
                    .build();
            when(userRepository.save(any(User.class))).thenReturn(persisted);

            UserDTO result = userService.saveUser(input);

            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.name()).isEqualTo("Maria");
            assertThat(result.email()).isEqualTo("maria@example.com");
            assertThat(result.password()).isNull();
            assertThat(result.roleId()).isEqualTo(1L);
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        private UpdateUserDTO input;

        @BeforeEach
        void setUp() {
            input = new UpdateUserDTO("Novo Nome", "novo@example.com");
        }

        @Test
        @DisplayName("deve atualizar usuário")
        void testUpdateUser() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
            when(userRepository.save(defaultUser)).thenReturn(defaultUser);

            UserDTO result = userService.updateUser(1L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(result.email()).isEqualTo("novo@example.com");
            assertThat(defaultUser.getName()).isEqualTo("Novo Nome");
            assertThat(defaultUser.getEmail()).isEqualTo("novo@example.com");
            assertThat(defaultUser.getPassword()).isEqualTo("hash1");
            assertThat(defaultUser.getRole()).isSameAs(defaultRole);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(99L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("updateUserRole")
    class UpdateUserRole {

        @Test
        @DisplayName("deve atualizar cargo do usuário")
        void testUpdateUserRole() {
            Role newRole = new Role("ADMIN");
            newRole.setId(2L);
            UpdateUserRoleDTO input = new UpdateUserRoleDTO(2L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
            when(roleService.findById(2L)).thenReturn(newRole);
            when(userRepository.save(defaultUser)).thenReturn(defaultUser);

            UserDTO result = userService.updateUserRole(1L, input);

            assertThat(result.roleId()).isEqualTo(2L);
            assertThat(defaultUser.getRole()).isSameAs(newRole);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando usuário não existir")
        void testThrowEntityNotFoundExceptionWhenUserDoesNotExist() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUserRole(99L, new UpdateUserRoleDTO(2L)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando cargo não existir")
        void testThrowEntityNotFoundExceptionWhenRoleDoesNotExist() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));
            when(roleService.findById(99L)).thenThrow(new EntityNotFoundException("Cargo não encontrado: 99"));

            assertThatThrownBy(() -> userService.updateUserRole(1L, new UpdateUserRoleDTO(99L)))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }
}
