package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.CisternDTO;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.dto.MemberDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
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
    private SystemSettingsService systemSettingsService;

    @InjectMocks
    private FamilyService familyService;

    @Nested
    @DisplayName("save")
    class Save {

        @Test
        @DisplayName("deve delegar persistência ao repositório")
        void deveDelegarPersistenciaAoRepositorio() {
            Family family = new Family("Silva", true, BigDecimal.valueOf(-8), BigDecimal.valueOf(-36));

            familyService.save(family);

            verify(familyRepository).save(family);
        }
    }

    @Nested
    @DisplayName("getFamilyById")
    class GetFamilyById {

        @Test
        @DisplayName("deve retornar família quando existir")
        void deveRetornarFamiliaQuandoExistir() {
            Family family = familyWithId(1L, "Silva", true);
            when(familyRepository.findById(1L)).thenReturn(Optional.of(family));

            Family result = familyService.getFamilyById(1L);

            assertThat(result).isSameAs(family);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void deveLancarEntityNotFoundExceptionQuandoIdNaoExistir() {
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
        void devePersistirFamiliaComMembrosECisternasERetornarDto() {
            MemberDTO m1 = new MemberDTO(null, "Ana", 32, false);
            CisternDTO c1 = new CisternDTO(null, new BigDecimal("5000"), new BigDecimal("2000"));
            FamilyDTO input = new FamilyDTO(
                    null,
                    "Família Souza",
                    false,
                    BigDecimal.valueOf(-7.5),
                    BigDecimal.valueOf(-35.2),
                    Family.FamilyStatus.NORMAL,
                    List.of(m1),
                    List.of(c1),
                    null,
                    null,
                    null);

            when(familyRepository.save(any(Family.class)))
                    .thenAnswer(invocation -> familyWithId(10L, (Family) invocation.getArgument(0)));

            FamilyDTO result = familyService.saveFamily(input);

            assertThat(result.id()).isEqualTo(10L);
            assertThat(result.name()).isEqualTo("Família Souza");
            assertThat(result.hasGutterSystem()).isFalse();
            assertThat(result.members()).hasSize(1);
            assertThat(result.members().getFirst().name()).isEqualTo("Ana");
            assertThat(result.cisterns()).hasSize(1);
            assertThat(result.cisterns().getFirst().capacityLiters()).isEqualByComparingTo("5000");

            ArgumentCaptor<Family> captor = ArgumentCaptor.forClass(Family.class);
            verify(familyRepository).save(captor.capture());
            Family saved = captor.getValue();
            assertThat(saved.getMembers()).hasSize(1);
            assertThat(saved.getCisterns()).hasSize(1);
            assertThat(saved.getCisterns().getFirst().getFamily()).isSameAs(saved);
        }
    }

    @Nested
    @DisplayName("updateFamily")
    class UpdateFamily {

        @Test
        @DisplayName("deve substituir dados, membros e cisternas")
        void deveSubstituirDadosMembrosECisternas() {
            Family existing = familyWithId(2L, "Antigo", true);
            existing.addMember(new Member("Velho", 60, false));
            existing.addCistern(new Cistern(new BigDecimal("1000"), new BigDecimal("100"), existing));

            when(familyRepository.findById(2L)).thenReturn(Optional.of(existing));
            when(familyRepository.save(existing)).thenReturn(existing);

            MemberDTO m1 = new MemberDTO(null, "Novo", 10, false);
            CisternDTO c1 = new CisternDTO(null, new BigDecimal("2000"), new BigDecimal("500"));
            FamilyDTO input = new FamilyDTO(
                    null,
                    "Novo Nome",
                    false,
                    BigDecimal.ONE,
                    BigDecimal.TEN,
                    Family.FamilyStatus.NORMAL,
                    List.of(m1),
                    List.of(c1),
                    null,
                    null,
                    null);

            FamilyDTO result = familyService.updateFamily(2L, input);

            assertThat(result.name()).isEqualTo("Novo Nome");
            assertThat(existing.getName()).isEqualTo("Novo Nome");
            assertThat(existing.getMembers()).hasSize(1);
            assertThat(existing.getMembers().getFirst().getName()).isEqualTo("Novo");
            assertThat(existing.getCisterns()).hasSize(1);
            assertThat(existing.getCisterns().getFirst().getCapacityLiters()).isEqualByComparingTo("2000");
            verify(familyRepository).save(existing);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando id não existir")
        void deveLancarEntityNotFoundExceptionQuandoIdNaoExistir() {
            when(familyRepository.findById(404L)).thenReturn(Optional.empty());
            FamilyDTO input = minimalFamilyDto("X");

            assertThatThrownBy(() -> familyService.updateFamily(404L, input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("404");
        }
    }

    @Nested
    @DisplayName("findFamilyById")
    class FindFamilyById {

        @Test
        @DisplayName("deve calcular consumo diário, dias restantes e data prevista")
        void deveCalcularConsumoDiarioDiasRestantesEDataPrevista() {
            Family family = familyWithId(1L, "Teste", true);
            family.addMember(new Member("A", 20, false));
            family.addMember(new Member("B", 25, false));
            family.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("100"), family));
            family.addCistern(new Cistern(new BigDecimal("10000"), new BigDecimal("50"), family));

            when(familyRepository.findById(1L)).thenReturn(Optional.of(family));
            SystemSettings settings = new SystemSettings();
            settings.setDailyWaterConsumption(new BigDecimal("14"));
            when(systemSettingsService.getSystemSettings()).thenReturn(settings);

            FamilyDTO result = familyService.findFamilyById(1L);

            assertThat(result.dailyConsumption()).isEqualByComparingTo("28");
            assertThat(result.remainingDays()).isEqualTo(5);
            assertThat(result.nextDeliveryDate()).isEqualTo(LocalDate.now().plusDays(5));
        }
    }

    @Nested
    @DisplayName("consultas paginadas")
    class ConsultasPaginadas {

        private final Pageable pageable = PageRequest.of(0, 5);

        @Test
        @DisplayName("findAllFamilies deve mapear página para FamilyDTO")
        void findAllFamiliesDeveMapearPagina() {
            Family f = familyWithId(1L, "A", false);
            when(familyRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(f)));

            Page<FamilyDTO> page = familyService.findAllFamilies(pageable);

            assertThat(page.getContent()).hasSize(1);
            assertThat(page.getContent().getFirst().id()).isEqualTo(1L);
        }

        @Test
        @DisplayName("findFamiliesByName deve delegar ao repositório")
        void findFamiliesByNameDeveDelegar() {
            when(familyRepository.findByNameContainingIgnoreCase(eq("sil"), eq(pageable)))
                    .thenReturn(Page.empty());

            Page<FamilyDTO> page = familyService.findFamiliesByName("sil", pageable);

            assertThat(page).isEmpty();
            verify(familyRepository).findByNameContainingIgnoreCase("sil", pageable);
        }

        @Test
        @DisplayName("findFamiliesByStatus deve delegar ao repositório")
        void findFamiliesByStatusDeveDelegar() {
            when(familyRepository.findByFamilyStatus(Family.FamilyStatus.URGENT, pageable))
                    .thenReturn(Page.empty());

            Page<FamilyDTO> page = familyService.findFamiliesByStatus(Family.FamilyStatus.URGENT, pageable);

            assertThat(page).isEmpty();
            verify(familyRepository).findByFamilyStatus(Family.FamilyStatus.URGENT, pageable);
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelAsc deve delegar ao repositório")
        void findAllOrderByCisternLevelAscDeveDelegar() {
            when(familyRepository.findAllOrderByCisternLevelAsc(pageable)).thenReturn(Page.empty());

            assertThat(familyService.findAllOrderByCisternLevelAsc(pageable)).isEmpty();
            verify(familyRepository).findAllOrderByCisternLevelAsc(pageable);
        }

        @Test
        @DisplayName("findAllOrderByCisternLevelDesc deve delegar ao repositório")
        void findAllOrderByCisternLevelDescDeveDelegar() {
            when(familyRepository.findAllOrderByCisternLevelDesc(pageable)).thenReturn(Page.empty());

            assertThat(familyService.findAllOrderByCisternLevelDesc(pageable)).isEmpty();
            verify(familyRepository).findAllOrderByCisternLevelDesc(pageable);
        }
    }

    @Nested
    @DisplayName("updateAllCisternLevels")
    class UpdateAllCisternLevels {

        @Test
        @DisplayName("deve consumir água das cisternas conforme consumo diário e persistir famílias")
        void deveConsumirAguaDasCisternasEPersistirFamilias() {
            SystemSettings settings = new SystemSettings();
            settings.setDailyWaterConsumption(new BigDecimal("20"));
            when(systemSettingsService.getSystemSettings()).thenReturn(settings);

            Family family = familyWithId(1L, "F", true);
            family.addMember(new Member("M", 30, false));
            Cistern c = new Cistern(new BigDecimal("1000"), new BigDecimal("100"), family);
            family.addCistern(c);

            when(familyRepository.findAll()).thenReturn(List.of(family));

            familyService.updateAllCisternLevels();

            assertThat(c.getCurrentLevelLiters()).isEqualByComparingTo("80");
            verify(familyRepository).saveAll(List.of(family));
        }
    }

    @Nested
    @DisplayName("calculateRemainingDays")
    class CalculateRemainingDays {

        @Test
        @DisplayName("deve retornar Integer.MAX_VALUE quando consumo diário for zero")
        void deveRetornarMaxValueQuandoConsumoZero() {
            Cistern cistern = new Cistern(new BigDecimal("1000"), new BigDecimal("500"), new Family());

            int days = familyService.calculateRemainingDays(cistern, BigDecimal.ZERO);

            assertThat(days).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("deve retornar divisão inteira por baixo do nível pelo consumo diário")
        void deveRetornarDivisaoInteiraPorBaixo() {
            Family f = new Family();
            Cistern cistern = new Cistern(new BigDecimal("1000"), new BigDecimal("100"), f);

            int days = familyService.calculateRemainingDays(cistern, new BigDecimal("30"));

            assertThat(days).isEqualTo(3);
        }
    }

    private static FamilyDTO minimalFamilyDto(String name) {
        MemberDTO m = new MemberDTO(null, "M", 20, false);
        CisternDTO c = new CisternDTO(null, new BigDecimal("1000"), new BigDecimal("100"));
        return new FamilyDTO(
                null,
                name,
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                Family.FamilyStatus.NORMAL,
                List.of(m),
                List.of(c),
                null,
                null,
                null);
    }

    private static Family familyWithId(Long id, String name, boolean gutter) {
        Family family = new Family(name, gutter, BigDecimal.valueOf(-8), BigDecimal.valueOf(-36));
        setField(family, "id", id);
        return family;
    }

    /**
     * Copia estado da família recebida do save (nome, listas) e aplica id para simular retorno JPA.
     */
    private static Family familyWithId(Long id, Family source) {
        Family family =
                new Family(source.getName(), source.isHasGutterSystem(), source.getLatitude(), source.getLongitude());
        setField(family, "id", id);
        for (Member m : source.getMembers()) {
            family.addMember(new Member(m.getName(), m.getAge(), m.isBedridden()));
        }
        for (Cistern c : source.getCisterns()) {
            family.addCistern(new Cistern(c.getCapacityLiters(), c.getCurrentLevelLiters(), family));
        }
        return family;
    }

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
