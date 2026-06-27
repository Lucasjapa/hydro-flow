package br.com.project.hydroflow.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.project.hydroflow.domain.Family.FamilyStatus;
import br.com.project.hydroflow.dto.CisternDTO;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.dto.MemberDTO;
import br.com.project.hydroflow.security.JwtService;
import br.com.project.hydroflow.security.UserDetailsServiceImpl;
import br.com.project.hydroflow.service.FamilyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FamilyController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Testes para FamilyController")
class FamilyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private FamilyService familyService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsServiceImpl userDetailsService;

    @Nested
    @DisplayName("createFamily")
    class CreateFamily {

        @Test
        @DisplayName("deve criar uma nova família")
        void testCreateNewFamily() throws Exception {
            List<MemberDTO> members = List.of(new MemberDTO(1L, "Membro", 30, false));
            List<CisternDTO> cisterns = List.of(new CisternDTO(1L, BigDecimal.valueOf(1000.0), BigDecimal.ZERO));
            FamilyDTO request = new FamilyDTO(
                    null,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    members,
                    cisterns,
                    null,
                    null,
                    null);
            FamilyDTO response = new FamilyDTO(
                    1L,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    members,
                    cisterns,
                    null,
                    null,
                    null);

            when(familyService.saveFamily(any(FamilyDTO.class))).thenReturn(response);

            mockMvc.perform(post("/hf/families")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.name").value("Familia Teste"));
        }
    }

    @Nested
    @DisplayName("updateFamily")
    class UpdateFamily {

        @Test
        @DisplayName("deve atualizar uma família")
        void testUpdateExistingFamily() throws Exception {
            List<MemberDTO> members = List.of(new MemberDTO(1L, "Membro", 30, false));
            List<CisternDTO> cisterns = List.of(new CisternDTO(1L, BigDecimal.valueOf(1000.0), BigDecimal.ZERO));
            FamilyDTO request = new FamilyDTO(
                    1L,
                    "Familia Atualizada",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    members,
                    cisterns,
                    null,
                    null,
                    null);

            when(familyService.updateFamily(eq(1L), any(FamilyDTO.class))).thenReturn(request);

            mockMvc.perform(put("/hf/families/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Familia Atualizada"));
        }
    }

    @Nested
    @DisplayName("findFamilyById")
    class FindFamilyById {

        @Test
        @DisplayName("deve buscar uma família pelo ID")
        void testFindFamilyById() throws Exception {
            FamilyDTO response = new FamilyDTO(
                    1L,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);

            when(familyService.findFamilyById(1L)).thenReturn(response);

            mockMvc.perform(get("/hf/families/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Familia Teste"));
        }
    }

    @Nested
    @DisplayName("findAllFamilies")
    class FindAllFamilies {

        @Test
        @DisplayName("deve listar todas as famílias com paginação")
        void testListAllFamilies() throws Exception {
            FamilyDTO family = new FamilyDTO(
                    1L,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);
            Page<FamilyDTO> page = new PageImpl<>(List.of(family));

            when(familyService.findAllFamilies(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/hf/families"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Familia Teste"));
        }
    }

    @Nested
    @DisplayName("findFamiliesByName")
    class FindFamiliesByName {

        @Test
        @DisplayName("deve filtrar pelo nome")
        void testFilterFamiliesByName() throws Exception {
            FamilyDTO family = new FamilyDTO(
                    1L,
                    "Familia Filtro",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);
            Page<FamilyDTO> page = new PageImpl<>(List.of(family));

            when(familyService.findFamiliesByName(eq("Filtro"), any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/hf/families").param("name", "Filtro"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name").value("Familia Filtro"));
        }
    }

    @Nested
    @DisplayName("findFamiliesByStatus")
    class FindFamiliesByStatus {

        @Test
        @DisplayName("deve filtrar por status")
        void testFilterFamiliesByStatus() throws Exception {
            FamilyDTO family = new FamilyDTO(
                    1L,
                    "Familia URGENTE",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.URGENT,
                    true,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);
            Page<FamilyDTO> page = new PageImpl<>(List.of(family));

            when(familyService.findFamiliesByStatus(eq(FamilyStatus.URGENT), any(Pageable.class)))
                    .thenReturn(page);

            mockMvc.perform(get("/hf/families").param("status", "URGENT"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].familyStatus").value("URGENT"));
        }
    }

    @Nested
    @DisplayName("activateFamily")
    class ActivateFamily {

        @Test
        @DisplayName("deve ativar uma família")
        void testActivateFamily() throws Exception {
            FamilyDTO response = new FamilyDTO(
                    1L,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    true,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);

            when(familyService.activateFamily(1L)).thenReturn(response);

            mockMvc.perform(patch("/hf/families/1/activate"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.active").value(true));
        }
    }

    @Nested
    @DisplayName("deactivateFamily")
    class DeactivateFamily {

        @Test
        @DisplayName("deve desativar uma família")
        void testDeactivateFamily() throws Exception {
            FamilyDTO response = new FamilyDTO(
                    1L,
                    "Familia Teste",
                    true,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    FamilyStatus.NORMAL,
                    false,
                    List.of(),
                    List.of(),
                    null,
                    null,
                    null);

            when(familyService.deactivateFamily(1L)).thenReturn(response);

            mockMvc.perform(patch("/hf/families/1/deactivate"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.active").value(false));
        }
    }
}
