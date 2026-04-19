package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.LoginDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Contrato HTTP — AuthController")
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

    @Test
    @DisplayName("POST /hf/auth/login retorna 403 e FirstAccessDTO quando usuário está em primeiro acesso")
    void loginPrimeiroAcessoRetorna403() throws Exception {
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

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
