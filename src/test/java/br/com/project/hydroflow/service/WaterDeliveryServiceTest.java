package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.Cistern;
import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.domain.WaterDelivery;
import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.repository.WaterDeliveryRepository;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para WaterDeliveryService")
class WaterDeliveryServiceTest {

    @Mock
    private WaterDeliveryRepository waterDeliveryRepository;

    @Mock
    private FamilyService familyService;

    @Mock
    private SystemSettingsService systemSettingsService;

    @InjectMocks
    private WaterDeliveryService waterDeliveryService;

    @Nested
    @DisplayName("saveWaterDelivery")
    class SaveWaterDelivery {

        @Test
        @DisplayName("deve distribuir água na cisterna e persistir entrega com volume efetivo")
        void deveDistribuirAguaNaPrimeiraCisternaEPersistirEntrega() {
            SystemSettings settings = settingsWithDaily(new BigDecimal("10"));
            when(systemSettingsService.getSystemSettings()).thenReturn(settings);

            Family family = familyWithId(1L, "Silva", true);
            family.addMember(new Member("Ana", 30, false));
            Cistern cistern = new Cistern(new BigDecimal("1000"), BigDecimal.ZERO, family);
            family.addCistern(cistern);

            when(familyService.getFamilyById(1L)).thenReturn(family);
            when(familyService.calculateRemainingDays(any(Cistern.class), any(BigDecimal.class)))
                    .thenReturn(30);

            stubSaveWaterDeliveryReturnsWithId(100L);

            WaterDeliveryDTO input = new WaterDeliveryDTO(
                    null, LocalDate.of(2024, 6, 1), new BigDecimal("1000"), new BigDecimal("500"), 1L);

            WaterDeliveryDTO result = waterDeliveryService.saveWaterDelivery(input);

            assertThat(result.id()).isEqualTo(100L);
            assertThat(result.familyId()).isEqualTo(1L);
            assertThat(result.deliveredAmountLiters()).isEqualByComparingTo("500");
            assertThat(cistern.getCurrentLevelLiters()).isEqualByComparingTo("500");

            verify(familyService).save(family);
            ArgumentCaptor<WaterDelivery> deliveryCaptor = ArgumentCaptor.forClass(WaterDelivery.class);
            verify(waterDeliveryRepository).save(deliveryCaptor.capture());
            WaterDelivery saved = deliveryCaptor.getValue();
            assertThat(saved.getDeliveryDate()).isEqualTo(input.deliveryDate());
            assertThat(saved.getRequestedAmountLiters()).isEqualByComparingTo("1000");
            assertThat(saved.getDeliveredAmountLiters()).isEqualByComparingTo("500");
            assertThat(saved.getFamily()).isSameAs(family);

            verify(familyService).calculateRemainingDays(any(Cistern.class), eq(new BigDecimal("10")));
        }

        @Test
        @DisplayName("deve preencher várias cisternas até esgotar o volume entregue")
        void devePreencherVariasCisternasAteEsgotarVolume() {
            when(systemSettingsService.getSystemSettings()).thenReturn(settingsWithDaily(new BigDecimal("5")));
            Family family = familyWithId(2L, "Souza", false);
            family.addMember(new Member("B", 20, false));
            family.addCistern(new Cistern(new BigDecimal("100"), new BigDecimal("80"), family));
            family.addCistern(new Cistern(new BigDecimal("200"), BigDecimal.ZERO, family));

            when(familyService.getFamilyById(2L)).thenReturn(family);
            when(familyService.calculateRemainingDays(any(Cistern.class), any(BigDecimal.class)))
                    .thenReturn(10);

            stubSaveWaterDeliveryReturnsWithId(200L);

            WaterDeliveryDTO input = new WaterDeliveryDTO(
                    null, LocalDate.of(2024, 7, 10), new BigDecimal("200"), new BigDecimal("50"), 2L);

            waterDeliveryService.saveWaterDelivery(input);

            assertThat(family.getCisterns().get(0).getCurrentLevelLiters()).isEqualByComparingTo("100");
            assertThat(family.getCisterns().get(1).getCurrentLevelLiters()).isEqualByComparingTo("30");

            ArgumentCaptor<WaterDelivery> deliveryCaptor = ArgumentCaptor.forClass(WaterDelivery.class);
            verify(waterDeliveryRepository).save(deliveryCaptor.capture());
            assertThat(deliveryCaptor.getValue().getDeliveredAmountLiters()).isEqualByComparingTo("50");
            verify(familyService, times(2)).calculateRemainingDays(any(Cistern.class), eq(new BigDecimal("5")));
        }

        @Test
        @DisplayName("deve limitar volume efetivo à capacidade disponível das cisternas")
        void deveLimitarVolumeEfetivoACapacidadeDisponivel() {
            when(systemSettingsService.getSystemSettings()).thenReturn(settingsWithDaily(new BigDecimal("10")));
            Family family = familyWithId(3L, "Lima", true);
            family.addMember(new Member("C", 40, false));
            family.addCistern(new Cistern(new BigDecimal("100"), BigDecimal.ZERO, family));

            when(familyService.getFamilyById(3L)).thenReturn(family);
            when(familyService.calculateRemainingDays(any(Cistern.class), any(BigDecimal.class)))
                    .thenReturn(5);

            stubSaveWaterDeliveryReturnsWithId(300L);

            WaterDeliveryDTO input = new WaterDeliveryDTO(
                    null, LocalDate.of(2025, 1, 1), new BigDecimal("500"), new BigDecimal("150"), 3L);

            WaterDeliveryDTO result = waterDeliveryService.saveWaterDelivery(input);

            assertThat(family.getCisterns().getFirst().getCurrentLevelLiters()).isEqualByComparingTo("100");
            assertThat(result.deliveredAmountLiters()).isEqualByComparingTo("100");
        }

        @Test
        @DisplayName("deve considerar consumo diário multiplicado pelo número de membros ao calcular dias restantes")
        void deveConsiderarConsumoProporcionalAoNumeroDeMembros() {
            when(systemSettingsService.getSystemSettings()).thenReturn(settingsWithDaily(new BigDecimal("7")));
            Family family = familyWithId(4L, "Costa", true);
            family.addMember(new Member("D1", 10, false));
            family.addMember(new Member("D2", 12, false));
            family.addCistern(new Cistern(new BigDecimal("500"), new BigDecimal("100"), family));

            when(familyService.getFamilyById(4L)).thenReturn(family);
            when(familyService.calculateRemainingDays(any(Cistern.class), any(BigDecimal.class)))
                    .thenReturn(20);

            stubSaveWaterDeliveryReturnsWithId(400L);

            WaterDeliveryDTO input = new WaterDeliveryDTO(
                    null, LocalDate.of(2024, 12, 12), new BigDecimal("100"), new BigDecimal("50"), 4L);

            waterDeliveryService.saveWaterDelivery(input);

            verify(familyService).calculateRemainingDays(any(Cistern.class), eq(new BigDecimal("14")));
        }
    }

    @Nested
    @DisplayName("findByYearAndFamilyId")
    class FindByYearAndFamilyId {

        @Test
        @DisplayName("deve mapear entregas do repositório para DTOs")
        void deveMapearEntregasParaDtos() {
            Family family = familyWithId(1L, "X", false);
            WaterDelivery w1 = waterDeliveryWithId(
                    10L, LocalDate.of(2024, 3, 1), new BigDecimal("100"), new BigDecimal("90"), family);
            WaterDelivery w2 = waterDeliveryWithId(
                    11L, LocalDate.of(2024, 5, 20), new BigDecimal("200"), new BigDecimal("200"), family);

            when(waterDeliveryRepository.findByYearAndFamilyId(2024, 1L)).thenReturn(List.of(w1, w2));

            List<WaterDeliveryDTO> result = waterDeliveryService.findByYearAndFamilyId(2024, 1L);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(10L);
            assertThat(result.get(0).deliveredAmountLiters()).isEqualByComparingTo("90");
            assertThat(result.get(1).familyId()).isEqualTo(1L);
            verify(waterDeliveryRepository).findByYearAndFamilyId(2024, 1L);
        }

        @Test
        @DisplayName("deve retornar lista vazia quando não houver entregas")
        void deveRetornarListaVaziaQuandoNaoHouverEntregas() {
            when(waterDeliveryRepository.findByYearAndFamilyId(2023, 99L)).thenReturn(List.of());

            List<WaterDeliveryDTO> result = waterDeliveryService.findByYearAndFamilyId(2023, 99L);

            assertThat(result).isEmpty();
        }
    }

    private void stubSaveWaterDeliveryReturnsWithId(long id) {
        when(waterDeliveryRepository.save(any(WaterDelivery.class))).thenAnswer(invocation -> {
            WaterDelivery wd = invocation.getArgument(0);
            setField(wd, "id", id);
            return wd;
        });
    }

    private static SystemSettings settingsWithDaily(BigDecimal daily) {
        SystemSettings settings = new SystemSettings();
        settings.setDailyWaterConsumption(daily);
        return settings;
    }

    private static Family familyWithId(Long id, String name, boolean gutter) {
        Family family = new Family(name, gutter, BigDecimal.valueOf(-8), BigDecimal.valueOf(-36));
        setField(family, "id", id);
        return family;
    }

    private static WaterDelivery waterDeliveryWithId(
            Long id, LocalDate date, BigDecimal requested, BigDecimal delivered, Family family) {
        WaterDelivery wd = new WaterDelivery(date, requested, delivered, family);
        setField(wd, "id", id);
        return wd;
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
