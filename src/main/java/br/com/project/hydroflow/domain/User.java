package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "hf_user")
public class User implements UserDetails {

    @Id
    @SequenceGenerator(name = "hf_user_id", sequenceName = "hf_user_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_user_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_role")
    private Role role;

    @Column(name = "first_access", nullable = false)
    private boolean firstAccess = true;

    @Column(name = "password_reset_token")
    private String passwordResetToken;

    @Column(name = "password_reset_token_expires_at")
    private LocalDateTime passwordResetTokenExpiresAt;

    public User() {}

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return List.of();

        return role.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isFirstAccess() {
        return firstAccess;
    }

    public void setFirstAccess(Boolean firstAccess) {
        this.firstAccess = firstAccess;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiresAt() {
        return passwordResetTokenExpiresAt;
    }

    public void setPasswordResetTokenExpiresAt(LocalDateTime passwordResetTokenExpiresAt) {
        this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String email;
        private String password;
        private Role role;
        private boolean firstAccess = true;
        private String passwordResetToken;
        private LocalDateTime passwordResetTokenExpiresAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder role(Role role) {
            this.role = role;
            return this;
        }

        public Builder firstAccess(boolean firstAccess) {
            this.firstAccess = firstAccess;
            return this;
        }

        public Builder passwordResetToken(String passwordResetToken) {
            this.passwordResetToken = passwordResetToken;
            return this;
        }

        public Builder passwordResetTokenExpiresAt(LocalDateTime passwordResetTokenExpiresAt) {
            this.passwordResetTokenExpiresAt = passwordResetTokenExpiresAt;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.id;
            user.name = this.name;
            user.email = this.email;
            user.password = this.password;
            user.role = this.role;
            user.firstAccess = this.firstAccess;
            user.passwordResetToken = this.passwordResetToken;
            user.passwordResetTokenExpiresAt = this.passwordResetTokenExpiresAt;
            return user;
        }
    }
}
