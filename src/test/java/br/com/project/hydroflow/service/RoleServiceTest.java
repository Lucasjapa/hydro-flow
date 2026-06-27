package br.com.project.hydroflow.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.project.hydroflow.domain.Permission;
import br.com.project.hydroflow.domain.Role;
import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.repository.PermissionRepository;
import br.com.project.hydroflow.repository.RoleRepository;
import br.com.project.hydroflow.repository.UserRepository;
import br.com.project.hydroflow.security.Permissions;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para RoleService")
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RoleService roleService;

    @Nested
    @DisplayName("findAllRoles")
    class FindAllRoles {
        @Test
        @DisplayName("deve retornar lista de RoleDTO")
        void testReturnRoleDtoList() {
            Role role = new Role("ADMIN");
            when(roleRepository.findAll()).thenReturn(List.of(role));

            List<RoleDTO> result = roleService.findAllRoles();

            assertEquals(1, result.size());
            assertEquals("ADMIN", result.get(0).name());
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {
        @Test
        @DisplayName("deve retornar Role se existir")
        void testReturnRoleWhenExists() {
            Role role = new Role("ADMIN");
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

            Role result = roleService.findById(1L);

            assertNotNull(result);
            assertEquals("ADMIN", result.getName());
        }

        @Test
        @DisplayName("deve lancar EntityNotFoundException se nao existir")
        void testThrowEntityNotFoundExceptionWhenNotFound() {
            when(roleRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> roleService.findById(1L));
        }
    }

    @Nested
    @DisplayName("saveRole")
    class SaveRole {
        @Test
        @DisplayName("deve salvar e retornar RoleDTO")
        void testSaveAndReturnRoleDto() {
            PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
            RoleDTO roleDTO = new RoleDTO(null, "ADMIN", List.of(permDTO));

            Permission perm = new Permission();
            ReflectionTestUtils.setField(perm, "name", "READ");
            ReflectionTestUtils.setField(perm, "label", "Read");
            when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of(perm));

            Role savedRole = new Role("ADMIN");
            savedRole.getPermissions().add(perm);
            when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

            RoleDTO result = roleService.saveRole(roleDTO);

            assertNotNull(result);
            assertEquals("ADMIN", result.name());
        }

        @Test
        @DisplayName("deve lancar excecao se permissao nao existir")
        void testThrowExceptionWhenPermissionNotFound() {
            PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
            RoleDTO roleDTO = new RoleDTO(null, "ADMIN", List.of(permDTO));

            when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of());

            assertThrows(EntityNotFoundException.class, () -> roleService.saveRole(roleDTO));
        }
    }

    @Nested
    @DisplayName("updateRole")
    class UpdateRole {
        @Test
        @DisplayName("deve atualizar e retornar RoleDTO")
        void testUpdateAndReturnRoleDto() {
            Role role = new Role("USER");
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

            PermissionDTO permDTO = new PermissionDTO(1L, "READ", "Read");
            RoleDTO roleDTO = new RoleDTO(1L, "ADMIN", List.of(permDTO));

            Permission perm = new Permission();
            ReflectionTestUtils.setField(perm, "name", "READ");
            ReflectionTestUtils.setField(perm, "label", "Read");
            when(permissionRepository.findAllById(List.of(1L))).thenReturn(List.of(perm));

            when(roleRepository.save(any(Role.class))).thenReturn(role);

            RoleDTO result = roleService.updateRole(1L, roleDTO);

            assertNotNull(result);
            assertEquals("ADMIN", result.name());
        }

        @Test
        @DisplayName("deve lancar excecao ao remover ADMIN do unico cargo que a possui")
        void testThrowExceptionWhenRemovingAdminFromLastRole() {
            Permission adminPermission = new Permission();
            ReflectionTestUtils.setField(adminPermission, "id", 1L);
            ReflectionTestUtils.setField(adminPermission, "name", Permissions.ADMIN);
            ReflectionTestUtils.setField(adminPermission, "label", "Admin");

            Role role = new Role("ADMIN");
            ReflectionTestUtils.setField(role, "id", 1L);
            role.getPermissions().add(adminPermission);
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
            when(roleRepository.countByPermissions_Name(Permissions.ADMIN)).thenReturn(1L);

            PermissionDTO readPermission = new PermissionDTO(2L, "READ", "Read");
            RoleDTO roleDTO = new RoleDTO(1L, "ADMIN", List.of(readPermission));

            assertThrows(IllegalStateException.class, () -> roleService.updateRole(1L, roleDTO));
            verify(roleRepository, never()).save(any());
        }

        @Test
        @DisplayName("deve permitir remover ADMIN quando outro cargo tambem possui a permissao")
        void testAllowRemovingAdminWhenAnotherRoleHasIt() {
            Permission adminPermission = new Permission();
            ReflectionTestUtils.setField(adminPermission, "id", 1L);
            ReflectionTestUtils.setField(adminPermission, "name", Permissions.ADMIN);
            ReflectionTestUtils.setField(adminPermission, "label", "Admin");

            Role role = new Role("ADMIN");
            ReflectionTestUtils.setField(role, "id", 1L);
            role.getPermissions().add(adminPermission);
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
            when(roleRepository.countByPermissions_Name(Permissions.ADMIN)).thenReturn(2L);

            PermissionDTO readPermission = new PermissionDTO(2L, "READ", "Read");
            RoleDTO roleDTO = new RoleDTO(1L, "OPERADOR", List.of(readPermission));

            Permission readPerm = new Permission();
            ReflectionTestUtils.setField(readPerm, "id", 2L);
            ReflectionTestUtils.setField(readPerm, "name", "READ");
            ReflectionTestUtils.setField(readPerm, "label", "Read");
            when(permissionRepository.findAllById(List.of(2L))).thenReturn(List.of(readPerm));
            when(roleRepository.save(any(Role.class))).thenReturn(role);

            RoleDTO result = roleService.updateRole(1L, roleDTO);

            assertNotNull(result);
            verify(roleRepository, times(1)).save(role);
        }
    }

    @Nested
    @DisplayName("deleteRole")
    class DeleteRole {
        @Test
        @DisplayName("deve deletar cargo sem usuarios vinculados")
        void testDeleteRoleWithoutUsers() {
            Role role = new Role("ADMIN");
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
            when(userRepository.existsByRole_Id(1L)).thenReturn(false);

            roleService.deleteRole(1L);

            verify(roleRepository, times(1)).delete(role);
        }

        @Test
        @DisplayName("deve lancar excecao se houver usuarios vinculados")
        void testThrowExceptionWhenRoleHasUsers() {
            Role role = new Role("ADMIN");
            when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
            when(userRepository.existsByRole_Id(1L)).thenReturn(true);

            assertThrows(IllegalStateException.class, () -> roleService.deleteRole(1L));
            verify(roleRepository, never()).delete(any());
        }
    }
}
