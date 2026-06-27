package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.ChangePasswordDTO;
import br.com.project.hydroflow.dto.FirstAccessDTO;
import br.com.project.hydroflow.dto.ForgotPasswordDTO;
import br.com.project.hydroflow.dto.LoginDTO;
import br.com.project.hydroflow.dto.MessageDTO;
import br.com.project.hydroflow.dto.ResetPasswordDTO;
import br.com.project.hydroflow.dto.TokenDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.annotation.AuthenticatedOnly;
import br.com.project.hydroflow.service.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hf/auth")
@Tag(name = "Autenticação", description = "Endpoints de autenticação")
@ApiResponses({@ApiResponse(responseCode = "500", description = "Erro interno do servidor")})
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetService passwordResetService;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetService passwordResetService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza login e retorna token JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
        @ApiResponse(responseCode = "403", description = "Primeiro acesso - troca de senha necessária")
    })
    public ResponseEntity<?> login(@RequestBody @Valid LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password()));

        User user = userRepository
                .findByEmail(loginDTO.email())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        if (user.isFirstAccess()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new FirstAccessDTO(user.getId(), "Troque sua senha antes de continuar"));
        }

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(user)));
    }

    @PatchMapping("/change-password")
    @Operation(summary = "Troca a senha no primeiro acesso")
    public ResponseEntity<TokenDTO> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        User user =
                userRepository.findById(dto.userId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setFirstAccess(false);
        userRepository.save(user);

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(user)));
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicita recuperação de senha por e-mail")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação processada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MessageDTO> forgotPassword(@RequestBody @Valid ForgotPasswordDTO forgotPasswordDTO) {
        return ResponseEntity.ok(passwordResetService.requestPasswordReset(forgotPasswordDTO));
    }

    @PatchMapping("/reset-password")
    @Operation(summary = "Redefine a senha usando o token enviado por e-mail")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Token inválido ou expirado"),
        @ApiResponse(responseCode = "422", description = "Dados inválidos")
    })
    public ResponseEntity<TokenDTO> resetPassword(@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        return ResponseEntity.ok(passwordResetService.resetPassword(resetPasswordDTO));
    }

    @PatchMapping("/update-password")
    @AuthenticatedOnly
    @Operation(summary = "Troca a senha do usuário autenticado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Senha atual inválida"),
        @ApiResponse(responseCode = "401", description = "Não autenticado"),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<TokenDTO> updatePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        User user =
                userRepository.findById(dto.userId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new BadCredentialsException("Senha atual inválida");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(user)));
    }
}
