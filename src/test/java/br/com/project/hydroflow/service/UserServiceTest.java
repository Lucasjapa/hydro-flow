package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("saveUser")
    class SaveUser {

        @Test
        @DisplayName("deve buscar cargo, codificar senha, persistir e retornar DTO sem senha")
        void deveBuscarCargoCodificarSenhaPersistirERetornarDtoSemSenha() {
            Role role = roleWithId(1L, "USER");
            when(roleService.findById(1L)).thenReturn(role);

            UserDTO input = new UserDTO(null, "Maria", "maria@example.com", "senha1234", 1L);
            when(passwordEncoder.encode("senha1234")).thenReturn("hash-senha");

            User persisted = userWithId(10L, "Maria", "maria@example.com", "hash-senha", role);
            when(userRepository.save(any(User.class))).thenReturn(persisted);

            UserDTO result = userService.saveUser(input);

            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.name()).isEqualTo("Maria");
            assertThat(result.email()).isEqualTo("maria@example.com");
            assertThat(result.password()).isNull();
            assertThat(result.roleId()).isEqualTo(1L);

            ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
            verify(userRepository).save(userCaptor.capture());
            User saved = userCaptor.getValue();
            assertThat(saved.getName()).isEqualTo("Maria");
            assertThat(saved.getEmail()).isEqualTo("maria@example.com");
            assertThat(saved.getPassword()).isEqualTo("hash-senha");
            assertThat(saved.getRole()).isSameAs(role);

            verify(roleService).findById(1L);
            verify(passwordEncoder).encode("senha1234");
        }
    }

    @Nested
    @DisplayName("updateUser")
    class UpdateUser {

        @Test
        @DisplayName("deve atualizar nome e email e reencodar senha quando informada")
        void deveAtualizarNomeEmailEReencodarSenhaQuandoInformada() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(1L, "Antigo", "antigo@example.com", "hash-antigo", role);
            when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(passwordEncoder.encode("novaSenha12")).thenReturn("hash-nova");
            when(userRepository.save(existing)).thenReturn(existing);

            UserDTO input = new UserDTO(null, "Novo Nome", "novo@example.com", "novaSenha12", null);

            UserDTO result = userService.updateUser(1L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(result.email()).isEqualTo("novo@example.com");
            assertThat(existing.getPassword()).isEqualTo("hash-nova");
            assertThat(existing.getRole()).isSameAs(role);
            verify(passwordEncoder).encode("novaSenha12");
            verify(userRepository).save(existing);
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("não deve alterar senha quando password for null")
        void naoDeveAlterarSenhaQuandoPasswordForNull() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(2L, "User", "u@example.com", "hash-original", role);
            when(userRepository.findById(2L)).thenReturn(Optional.of(existing));
            when(userRepository.save(existing)).thenReturn(existing);

            UserDTO input = new UserDTO(null, "User X", "ux@example.com", null, null);

            userService.updateUser(2L, input);

            assertThat(existing.getPassword()).isEqualTo("hash-original");
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("não deve alterar senha quando password for em branco")
        void naoDeveAlterarSenhaQuandoPasswordForEmBranco() {
            Role role = roleWithId(2L, "USER");
            User existing = userWithId(3L, "User", "u@example.com", "hash-original", role);
            when(userRepository.findById(3L)).thenReturn(Optional.of(existing));
            when(userRepository.save(existing)).thenReturn(existing);

            UserDTO input = new UserDTO(null, "User X", "ux@example.com", "   ", null);

            userService.updateUser(3L, input);

            assertThat(existing.getPassword()).isEqualTo("hash-original");
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void deveLancarEntityNotFoundExceptionQuandoIdNaoExistir() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            UserDTO input = new UserDTO(null, "X", "x@example.com", "senha1234", 1L);

            assertThatThrownBy(() -> userService.updateUser(99L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");

            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
            verify(roleService, never()).findById(anyLong());
        }
    }

    private static Role roleWithId(Long id, String name) {
        Role role = new Role(name);
        role.setId(id);
        return role;
    }

    private static User userWithId(Long id, String name, String email, String password, Role role) {
        User user = new User(name, email, password, role);
        try {
            Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
        return user;
    }
}
