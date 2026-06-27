package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.ChangePasswordDTO;
import br.com.project.hydroflow.dto.ForgotPasswordDTO;
import br.com.project.hydroflow.dto.LoginDTO;
import br.com.project.hydroflow.dto.MessageDTO;
import br.com.project.hydroflow.dto.ResetPasswordDTO;
import br.com.project.hydroflow.dto.TokenDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.PasswordResetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes para AuthController")
class AuthControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private PasswordResetService passwordResetService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    @Nested
    @DisplayName("login")
    class Login {

        @Test
        @DisplayName("deve retornar 403 e FirstAccessDTO quando usuário está em primeiro acesso")
        void testReturn403ForFirstAccess() throws Exception {
            LoginDTO body = new LoginDTO("maria@example.com", "senha1234");

            User user = new User("Maria", "maria@example.com", "encoded", null);
            setField(user, "id", 42L);
            user.setFirstAccess(true);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mock(Authentication.class));
            when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(user));

            mockMvc.perform(post("/hf/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.userId").value(42))
                    .andExpect(jsonPath("$.message").value("Troque sua senha antes de continuar"));

            verify(jwtService, never()).generateToken(any());
        }

        @Test
        @DisplayName("deve retornar 200 e TokenDTO quando sucesso")
        void testReturn200AndTokenDtoOnSuccess() throws Exception {
            LoginDTO body = new LoginDTO("maria@example.com", "senha1234");

            User user = new User("Maria", "maria@example.com", "encoded", null);
            setField(user, "id", 42L);
            user.setFirstAccess(false);

            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(mock(Authentication.class));
            when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(user));
            when(jwtService.generateToken(any())).thenReturn("token123");

            mockMvc.perform(post("/hf/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token123"));
        }
    }

    @Nested
    @DisplayName("changePassword")
    class ChangePassword {

        @Test
        @DisplayName("deve retornar 200 e TokenDTO quando sucesso")
        void testReturn200AndTokenDtoOnSuccess() throws Exception {
            ChangePasswordDTO body = new ChangePasswordDTO(42L, "senha1234", "novaSenha");

            User user = new User("Maria", "maria@example.com", "encoded", null);
            setField(user, "id", 42L);

            when(userRepository.findById(42L)).thenReturn(Optional.of(user));
            when(passwordEncoder.encode("novaSenha")).thenReturn("encodedNovaSenha");
            when(jwtService.generateToken(any())).thenReturn("token123");

            mockMvc.perform(patch("/hf/auth/change-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token123"));
        }
    }

    @Nested
    @DisplayName("forgotPassword")
    class ForgotPassword {

        @Test
        @DisplayName("deve retornar 200 e mensagem genérica")
        void testReturn200AndGenericMessage() throws Exception {
            ForgotPasswordDTO body = new ForgotPasswordDTO("maria@example.com");
            when(passwordResetService.requestPasswordReset(any(ForgotPasswordDTO.class)))
                    .thenReturn(new MessageDTO("Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha"));

            mockMvc.perform(post("/hf/auth/forgot-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message")
                            .value("Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha"));
        }
    }

    @Nested
    @DisplayName("resetPassword")
    class ResetPassword {

        @Test
        @DisplayName("deve retornar 200 e TokenDTO quando token for válido")
        void testReturn200AndTokenDtoWhenTokenIsValid() throws Exception {
            ResetPasswordDTO body = new ResetPasswordDTO("123456", "novaSenha123");
            when(passwordResetService.resetPassword(any(ResetPasswordDTO.class)))
                    .thenReturn(new TokenDTO("token123"));

            mockMvc.perform(patch("/hf/auth/reset-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token123"));
        }
    }

    @Nested
    @DisplayName("updatePassword")
    class UpdatePassword {

        @Test
        @DisplayName("deve retornar 200 e TokenDTO quando sucesso")
        void testReturn200AndTokenDtoOnSuccess() throws Exception {
            ChangePasswordDTO body = new ChangePasswordDTO(42L, "senha1234", "novaSenha");

            User user = new User("Maria", "maria@example.com", "encoded", null);
            setField(user, "id", 42L);

            when(userRepository.findById(42L)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("senha1234", "encoded")).thenReturn(true);
            when(passwordEncoder.encode("novaSenha")).thenReturn("encodedNovaSenha");
            when(jwtService.generateToken(any())).thenReturn("token123");

            mockMvc.perform(patch("/hf/auth/update-password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("token123"));
        }
    }
}
