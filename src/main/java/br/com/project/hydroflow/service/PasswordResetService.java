package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.ForgotPasswordDTO;
import br.com.project.hydroflow.dto.MessageDTO;
import br.com.project.hydroflow.dto.ResetPasswordDTO;
import br.com.project.hydroflow.dto.TokenDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final String RESET_REQUEST_MESSAGE =
            "Se o e-mail estiver cadastrado, você receberá instruções para redefinir sua senha";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public PasswordResetService(
            UserRepository userRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public MessageDTO requestPasswordReset(ForgotPasswordDTO forgotPasswordDTO) {
        log.info("Solicitação de recuperação de senha para: {}", forgotPasswordDTO.email());

        userRepository.findByEmail(forgotPasswordDTO.email()).ifPresent(user -> {
            String token = generateResetToken();
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiresAt(LocalDateTime.now().plusHours(1));
            userRepository.save(user);
            emailService.sendPasswordResetEmail(user.getEmail(), token);
        });

        return new MessageDTO(RESET_REQUEST_MESSAGE);
    }

    public TokenDTO resetPassword(ResetPasswordDTO resetPasswordDTO) {
        log.info("Redefinindo senha com token de recuperação");

        User user = userRepository
                .findByPasswordResetToken(resetPasswordDTO.token())
                .orElseThrow(() -> new BadCredentialsException("Token inválido ou expirado"));

        if (user.getPasswordResetTokenExpiresAt() == null
                || user.getPasswordResetTokenExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token de recuperação expirado para usuário id: {}", user.getId());
            throw new BadCredentialsException("Token inválido ou expirado");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordDTO.newPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetTokenExpiresAt(null);
        user.setFirstAccess(false);
        userRepository.save(user);

        log.info("Senha redefinida com sucesso para usuário id: {}", user.getId());
        return new TokenDTO(jwtService.generateToken(user));
    }

    private String generateResetToken() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }
}
