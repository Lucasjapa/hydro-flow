package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.CisternDTO;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.dto.MemberDTO;
import br.com.project.hydroflow.repository.CisternRepository;
import br.com.project.hydroflow.repository.FamilyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FamilyService")
class FamilyServiceTest {

    @Mock
    private FamilyRepository familyRepository;

    @Mock
    private CisternRepository cisternRepository;

    @Mock
    private SystemSettingsService systemSettingsService;

    private CisternService cisternService;

    private FamilyService familyService;

    private Pageable pageable;
    private Family defaultFamily;
    private Family urgentFamily;
    private SystemSettings systemSettings;

    @BeforeEach
    void setUp() {
        cisternService = new CisternService(cisternRepository);
        familyService = new FamilyService(familyRepository, systemSettingsService, cisternService);
        pageable = PageRequest.of(0, 5);

        this.defaultFamily = Family.builder()
                .id(1L)
                .name("Silva")
                .hasGutterSystem(true)
                .latitude(BigDecimal.valueOf(-8))
                .longitude(BigDecimal.valueOf(-36))
                .members(new ArrayList<>())
                .cisterns(new ArrayList<>())
                .build();

        defaultFamily.addMember(new Member("A", 20, false));
        defaultFamily.addMember(new Member("B", 25, false));
        defaultFamily.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("100"), defaultFamily));
        defaultFamily.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("50"), defaultFamily));

        // Familia 2
        this.urgentFamily = Family.builder()
                .id(2L)
                .name("Santos")
                .hasGutterSystem(false)
                .latitude(BigDecimal.valueOf(-9))
                .longitude(BigDecimal.valueOf(-37))
                .familyStatus(Family.FamilyStatus.URGENT)
                .members(new ArrayList<>())
                .cisterns(new ArrayList<>())
                .build();

        urgentFamily.addMember(new Member("C", 35, false));
        urgentFamily.addMember(new Member("D", 40, true));
        urgentFamily.addCistern(new Cistern(new BigDecimal("500"), new BigDecimal("20"), urgentFamily));

        systemSettings = SystemSettings.builder()
                .dailyWaterConsumption(new BigDecimal("14"))
                .build();
    }

    @Nested
    @DisplayName("getFamilyById")
    class GetFamilyById {

        @Test
        @DisplayName("deve retornar família quando existir")
        void testReturnFamilyWhenExists() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));

            Family result = familyService.getFamilyById(1L);

            assertThat(result).isSameAs(defaultFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.getFamilyById(99L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("99");
        }
    }

    @Nested
    @DisplayName("saveFamily")
    class SaveFamily {

        @Test
        @DisplayName("deve persistir família com membros e cisternas e retornar DTO")
        void testPersistFamilyWithMembersAndCisternsAndReturnDto() {
            FamilyDTO input = new FamilyDTO(
                    null,
                    "Família Souza",
                    false,
                    BigDecimal.valueOf(-8),
                    BigDecimal.valueOf(-36),
                    Family.FamilyStatus.NORMAL,
                    true,
                    List.of(new MemberDTO(null, "Ana", 32, false)),
                    List.of(new CisternDTO(null, new BigDecimal("5000"), new BigDecimal("2000"))),
                    null,
                    null,
                    null);

            when(familyRepository.save(any(Family.class))).thenReturn(defaultFamily);

            FamilyDTO result = familyService.saveFamily(input);

            verify(familyRepository).save(any(Family.class));
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.name()).isEqualTo("Silva");
            assertThat(result.hasGutterSystem()).isTrue();
            assertThat(result.members()).hasSize(2);
            assertThat(result.members().getFirst().name()).isEqualTo("A");
            assertThat(result.cisterns()).hasSize(2);
            assertThat(result.cisterns().getFirst().capacityLiters()).isEqualByComparingTo("10000");
        }
    }

    @Nested
    @DisplayName("updateFamily")
    class UpdateFamily {

        private FamilyDTO input;

        @BeforeEach
        void setUp() {
            input = new FamilyDTO(
                    null,
                    "Novo Nome",
                    false,
                    BigDecimal.ONE,
                    BigDecimal.TEN,
                    Family.FamilyStatus.NORMAL,
                    true,
                    List.of(new MemberDTO(null, "Novo", 10, false)),
                    List.of(new CisternDTO(null, new BigDecimal("2000"), new BigDecimal("500"))),
                    null,
                    null,
                    null);
        }

        @Test
        @DisplayName("deve substituir dados, membros e cisternas")
        void testReplaceDataMembersAndCisterns() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));
            when(familyRepository.save(defaultFamily)).thenReturn(defaultFamily);

            FamilyDTO result = familyService.updateFamily(1L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(defaultFamily.getName()).isEqualTo("Novo Nome");
            assertThat(defaultFamily.getMembers()).hasSize(1);
            assertThat(defaultFamily.getMembers().getFirst().getName()).isEqualTo("Novo");
            assertThat(defaultFamily.getCisterns()).hasSize(1);
            assertThat(defaultFamily.getCisterns().getFirst().getCapacityLiters())
                    .isEqualByComparingTo("2000");
            verify(familyRepository).save(defaultFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(404L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.updateFamily(404L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("activateFamily")
    class ActivateFamily {

        @Test
        @DisplayName("deve ativar família")
        void testActivateFamily() {
            defaultFamily.setActive(false);
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));
            when(familyRepository.save(defaultFamily)).thenReturn(defaultFamily);

            FamilyDTO result = familyService.activateFamily(1L);

            assertThat(defaultFamily.isActive()).isTrue();
            assertThat(result.active()).isTrue();
            verify(familyRepository).save(defaultFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(404L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.activateFamily(404L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("deactivateFamily")
    class DeactivateFamily {

        @Test
        @DisplayName("deve desativar família")
        void testDeactivateFamily() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));
            when(familyRepository.save(defaultFamily)).thenReturn(defaultFamily);

            FamilyDTO result = familyService.deactivateFamily(1L);

            assertThat(defaultFamily.isActive()).isFalse();
            assertThat(result.active()).isFalse();
            verify(familyRepository).save(defaultFamily);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void testThrowEntityNotFoundExceptionWhenIdDoesNotExist() {
            when(familyRepository.findById(404L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> familyService.deactivateFamily(404L))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("getFamilyDetails")
    class GetFamilyDetails {

        @Test
        @DisplayName("deve retornar DTO da família com consumo diário, dias restantes e data prevista")
        void testReturnFamilyDtoWithDailyConsumptionRemainingDaysAndNextDeliveryDate() {
            when(familyRepository.findById(1L)).thenReturn(Optional.of(defaultFamily));
            when(systemSettingsService.getSystemSettings()).thenReturn(systemSettings);

            FamilyDTO result = familyService.findFamilyById(1L);

            assertThat(result.dailyConsumption()).isEqualByComparingTo("28");
            assertThat(result.remainingDays()).isEqualTo(5);
            assertThat(result.nextDeliveryDate()).isEqualTo(LocalDate.now().plusDays(5));
        }
    }

    @Nested
    @DisplayName("consultas paginadas")
    class ConsultasPaginadas {

        @Test
        @DisplayName("findAllFamilies deve mapear página para FamilyDTO")
        void testFindAllFamiliesMapPageToFamilyDto() {
            when(familyRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(defaultFamily)));

            Page<FamilyDTO> page = familyService.findAllFamilies(pageable);

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Silva");
        }

        @Test
        @DisplayName("findFamiliesByName deve mapear resultado para FamilyDTO")
        void testFindFamiliesByNameMapToFamilyDto() {
            when(familyRepository.findByNameContainingIgnoreCase("sil", pageable))
                    .thenReturn(new PageImpl<>(List.of(defaultFamily)));

            Page<FamilyDTO> page = familyService.findFamiliesByName("sil", pageable);

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Silva");
        }

        @Test
        @DisplayName("findFamiliesByStatus deve mapear resultado para FamilyDTO")
        void testFindFamiliesByStatusMapToFamilyDto() {
            when(familyRepository.findByFamilyStatus(Family.FamilyStatus.NORMAL, pageable))
                    .thenReturn(new PageImpl<>(List.of(defaultFamily)));

            Page<FamilyDTO> page = familyService.findFamiliesByStatus(Family.FamilyStatus.NORMAL, pageable);

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Silva");
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelAsc deve mapear resultado para FamilyDTO")
        void testFindAllOrderByCisternLevelAscMapToFamilyDto() {
            when(familyRepository.findAllOrderByCisternLevelAsc(pageable))
                    .thenReturn(new PageImpl<>(List.of(urgentFamily, defaultFamily)));

            Page<FamilyDTO> page = familyService.findAllOrderByCisternLevelAsc(pageable);

            assertThat(page.getContent()).hasSize(2);
            assertThat(page.getContent().getFirst().id()).isEqualTo(2L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Santos");
            assertThat(page.getContent().getLast().id()).isEqualTo(1L);
            assertThat(page.getContent().getLast().name()).isEqualTo("Silva");
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelDesc deve mapear resultado para FamilyDTO")
        void testFindAllOrderByCisternLevelDescMapToFamilyDto() {
            when(familyRepository.findAllOrderByCisternLevelDesc(pageable))
                    .thenReturn(new PageImpl<>(List.of(defaultFamily, urgentFamily)));

            Page<FamilyDTO> page = familyService.findAllOrderByCisternLevelDesc(pageable);

            assertThat(page.getContent()).hasSize(2);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
            assertThat(page.getContent().getFirst().name()).isEqualTo("Silva");
            assertThat(page.getContent().getLast().id()).isEqualTo(2L);
            assertThat(page.getContent().getLast().name()).isEqualTo("Santos");
        }
    }

    @Nested
    @DisplayName("updateAllCisternLevels")
    class UpdateAllCisternLevels {

        @Test
        @DisplayName("deve consumir água das cisternas conforme consumo diário e persistir famílias")
        void testConsumeWaterFromCisternsAndPersistFamilies() {
            when(systemSettingsService.getSystemSettings()).thenReturn(systemSettings);
            when(familyRepository.findByActiveTrue()).thenReturn(List.of(defaultFamily));

            BigDecimal nivelInicial = defaultFamily.getCisterns().getFirst().getCurrentLevelLiters();

            familyService.updateAllCisternLevels();

            BigDecimal consumoEsperado = new BigDecimal("28");

            assertThat(defaultFamily.getCisterns().getFirst().getCurrentLevelLiters())
                    .isEqualByComparingTo(nivelInicial.subtract(consumoEsperado));

            verify(familyRepository).saveAll(List.of(defaultFamily));
        }
    }

    @Nested
    @DisplayName("calculateRemainingDays")
    class CalculateRemainingDays {

        @ParameterizedTest(name = "nível {0}L com consumo diário {1}L deve retornar {2} dias")
        @CsvSource({"100, 30, 3", "500, 10, 50", "999, 100, 9"})
        @DisplayName("deve calcular dias restantes conforme nível e consumo diário")
        void testCalculateRemainingDays(String currentLevel, String dailyConsumption, int expectedDays) {
            int days = cisternService.calculateRemainingDays(
                    new BigDecimal(currentLevel), new BigDecimal(dailyConsumption));

            assertThat(days).isEqualTo(expectedDays);
        }

        @Test
        @DisplayName("deve lançar IllegalStateException quando consumo diário for zero")
        void testThrowIllegalStateExceptionWhenDailyConsumptionIsZero() {
            assertThatThrownBy(() -> cisternService.calculateRemainingDays(new BigDecimal("500"), BigDecimal.ZERO))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Daily consumption must be greater than zero");
        }
    }
}
