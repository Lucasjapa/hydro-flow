package br.com.project.hydroflow.security.annotation;

import br.com.project.hydroflow.security.Permissions;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.security.access.prepost.PreAuthorize;

// Acesso para ADMIN ou permissão específica
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.ADMIN + "') or hasAuthority('" + Permissions.MANAGE_USERS + "')")
public @interface AdminOrManageUsers {}
