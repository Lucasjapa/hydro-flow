package br.com.project.hydroflow.controller;

import br.com.project.hydroflow.domain.User;
import br.com.project.hydroflow.dto.ChangePasswordDTO;
import br.com.project.hydroflow.dto.FirstAccessDTO;
import br.com.project.hydroflow.dto.LoginDTO;
import br.com.project.hydroflow.dto.TokenDTO;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.annotation.AuthenticatedOnly;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    @AuthenticatedOnly
    @Operation(summary = "Troca a senha no primeiro acesso")
    public ResponseEntity<TokenDTO> changePassword(@RequestBody @Valid ChangePasswordDTO dto) {
        User user =
                userRepository.findById(dto.userId()).orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        user.setFirstAccess(false);
        userRepository.save(user);

        return ResponseEntity.ok(new TokenDTO(jwtService.generateToken(user)));
    }
}
