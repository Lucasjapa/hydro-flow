package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.dto.PermissionDTO;
import br.com.project.hydroflow.dto.RoleDTO;
import br.com.project.hydroflow.dto.UpdateUserRoleDTO;
import br.com.project.hydroflow.dto.UserDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.PermissionService;
import br.com.project.hydroflow.service.RoleService;
import br.com.project.hydroflow.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = UserManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes para UserManagementController")
class UserManagementControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private PermissionService permissionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    @DisplayName("findAllUsers")
    class FindAllUsers {
        @Test
        @DisplayName("deve listar todos os usuários")
        void testListAllUsers() throws Exception {
            UserDTO user = new UserDTO(1L, "Admin", "admin@teste.com", "senha123", 1L);
            when(userService.findAllUsers()).thenReturn(List.of(user));

            mockMvc.perform(get("/hf/user-management"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("Admin"));
        }
    }

    @Nested
    @DisplayName("updateUserRole")
    class UpdateUserRole {
        @Test
        @DisplayName("deve atualizar cargo do usuário")
        void testUpdateUserRole() throws Exception {
            UpdateUserRoleDTO request = new UpdateUserRoleDTO(2L);
            UserDTO response = new UserDTO(1L, "Usuario", "usuario@teste.com", null, 2L);

            when(userService.updateUserRole(eq(1L), any(UpdateUserRoleDTO.class))).thenReturn(response);

            mockMvc.perform(put("/hf/user-management/users/1/role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roleId").value(2L));
        }
    }

    @Nested
    @DisplayName("findAllRoles")
    class FindAllRoles {
        @Test
        @DisplayName("deve listar todos os cargos")
        void testListAllRoles() throws Exception {
            RoleDTO role = new RoleDTO(1L, "ADMIN", List.of());
            when(roleService.findAllRoles()).thenReturn(List.of(role));

            mockMvc.perform(get("/hf/user-management/roles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("ADMIN"));
        }
    }

    @Nested
    @DisplayName("findAllPermissions")
    class FindAllPermissions {
        @Test
        @DisplayName("deve listar todas as permissões")
        void testListAllPermissions() throws Exception {
            PermissionDTO perm = new PermissionDTO(1L, "READ_ALL", "Ler tudo");
            when(permissionService.findAllPermissions()).thenReturn(List.of(perm));

            mockMvc.perform(get("/hf/user-management/permissions"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].name").value("READ_ALL"));
        }
    }

    @Nested
    @DisplayName("createRole")
    class CreateRole {
        @Test
        @DisplayName("deve criar cargo")
        void testCreateRole() throws Exception {
            RoleDTO request = new RoleDTO(null, "ADMIN", List.of());
            RoleDTO response = new RoleDTO(1L, "ADMIN", List.of());

            when(roleService.saveRole(any(RoleDTO.class))).thenReturn(response);

            mockMvc.perform(post("/hf/user-management/create-role")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L));
        }
    }

    @Nested
    @DisplayName("updateRole")
    class UpdateRole {
        @Test
        @DisplayName("deve atualizar cargo")
        void testUpdateRole() throws Exception {
            RoleDTO request = new RoleDTO(1L, "ADMIN", List.of());
            when(roleService.updateRole(eq(1L), any(RoleDTO.class))).thenReturn(request);

            mockMvc.perform(put("/hf/user-management/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("ADMIN"));
        }
    }

    @Nested
    @DisplayName("deleteRole")
    class DeleteRole {
        @Test
        @DisplayName("deve deletar cargo")
        void testDeleteRole() throws Exception {
            doNothing().when(roleService).deleteRole(1L);

            mockMvc.perform(delete("/hf/user-management/roles/1")).andExpect(status().isNoContent());
        }
    }
}
