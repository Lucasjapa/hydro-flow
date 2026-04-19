package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("authorizationService")
public class AuthorizationService {

    public boolean isAdminOrOwner(Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ADMIN") || a.getAuthority().equals("MANAGE_USERS"));
        return isAdmin || user.getId().equals(id);
    }
}
