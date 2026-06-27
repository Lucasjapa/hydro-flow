package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.ForgotPasswordDTO;
import br.com.project.hydroflow.dto.ResetPasswordDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para PasswordResetService")
class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private PasswordResetService passwordResetService;

    private User user;

    @BeforeEach
    void setUp() {
        passwordResetService = new PasswordResetService(userRepository, emailService, passwordEncoder, jwtService);

        user = User.builder()
                .id(1L)
                .name("Maria")
                .email("maria@example.com")
                .password("hash")
                .build();
    }

    @Nested
    @DisplayName("requestPasswordReset")
    class RequestPasswordReset {

        @Test
        @DisplayName("deve gerar token, salvar usuário e enviar e-mail quando e-mail existir")
        void testGenerateTokenSaveUserAndSendEmailWhenEmailExists() {
            when(userRepository.findByEmail("maria@example.com")).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            var result = passwordResetService.requestPasswordReset(new ForgotPasswordDTO("maria@example.com"));

            assertThat(result.message())
                    .isEqualTo("Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha");
            assertThat(user.getPasswordResetToken()).isNotBlank();
            assertThat(user.getPasswordResetToken()).matches("\\d{6}");
            assertThat(user.getPasswordResetTokenExpiresAt()).isAfter(LocalDateTime.now());
            verify(emailService).sendPasswordResetEmail(eq("maria@example.com"), eq(user.getPasswordResetToken()));
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("deve retornar mensagem genérica quando e-mail não existir")
        void testReturnGenericMessageWhenEmailDoesNotExist() {
            when(userRepository.findByEmail("inexistente@example.com")).thenReturn(Optional.empty());

            var result = passwordResetService.requestPasswordReset(new ForgotPasswordDTO("inexistente@example.com"));

            assertThat(result.message())
                    .isEqualTo("Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha");
            verify(emailService, never()).sendPasswordResetEmail(any(), any());
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("resetPassword")
    class ResetPassword {

        @Test
        @DisplayName("deve redefinir senha e retornar token quando token for válido")
        void testResetPasswordAndReturnTokenWhenTokenIsValid() {
            user.setPasswordResetToken("123456");
            user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusMinutes(30));

            when(userRepository.findByPasswordResetToken("123456")).thenReturn(Optional.of(user));
            when(passwordEncoder.encode("novaSenha123")).thenReturn("nova-hash");
            when(jwtService.generateToken(user)).thenReturn("jwt-token");
            when(userRepository.save(user)).thenReturn(user);

            var result = passwordResetService.resetPassword(new ResetPasswordDTO("123456", "novaSenha123"));

            assertThat(result.token()).isEqualTo("jwt-token");
            assertThat(user.getPassword()).isEqualTo("nova-hash");
            assertThat(user.getPasswordResetToken()).isNull();
            assertThat(user.getPasswordResetTokenExpiresAt()).isNull();
            assertThat(user.isFirstAccess()).isFalse();
            verify(userRepository).save(user);
        }

        @Test
        @DisplayName("deve lançar BadCredentialsException quando token não existir")
        void testThrowBadCredentialsExceptionWhenTokenDoesNotExist() {
            when(userRepository.findByPasswordResetToken("999999")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> passwordResetService.resetPassword(new ResetPasswordDTO("999999", "novaSenha123")))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Token inválido ou expirado");
        }

        @Test
        @DisplayName("deve lançar BadCredentialsException quando token estiver expirado")
        void testThrowBadCredentialsExceptionWhenTokenIsExpired() {
            user.setPasswordResetToken("654321");
            user.setPasswordResetTokenExpiresAt(LocalDateTime.now().minusMinutes(1));

            when(userRepository.findByPasswordResetToken("654321")).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> passwordResetService.resetPassword(new ResetPasswordDTO("654321", "novaSenha123")))
                    .isInstanceOf(BadCredentialsException.class)
                    .hasMessage("Token inválido ou expirado");
        }
    }
}
