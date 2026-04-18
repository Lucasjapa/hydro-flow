package br.com.project.hydroflow.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.SystemSettingsDTO;
import br.com.project.hydroflow.repository.SystemSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para SystemSettingsService")
class SystemSettingsServiceTest {

    @Mock
    private SystemSettingsRepository systemSettingsRepository;

    @InjectMocks
    private SystemSettingsService systemSettingsService;

    @Nested
    @DisplayName("getSystemSettings")
    class GetSystemSettings {

        @Test
        @DisplayName("deve retornar configurações quando existirem")
        void deveRetornarConfiguracoesQuandoExistirem() {
            SystemSettings entity = settingsWithId(1L, new BigDecimal("14.5"));
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.of(entity));

            SystemSettings result = systemSettingsService.getSystemSettings();

            assertThat(result).isSameAs(entity);
            verify(systemSettingsRepository).findById(1L);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando não existir registro")
        void deveLancarEntityNotFoundExceptionQuandoNaoExistirRegistro() {
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> systemSettingsService.getSystemSettings())
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Configurações não encontradas");

            verify(systemSettingsRepository).findById(1L);
        }
    }

    @Nested
    @DisplayName("findSystemSettings")
    class FindSystemSettings {

        @Test
        @DisplayName("deve retornar DTO correspondente à entidade persistida")
        void deveRetornarDtoCorrespondente() {
            SystemSettings entity = settingsWithId(1L, new BigDecimal("20"));
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.of(entity));

            SystemSettingsDTO dto = systemSettingsService.findSystemSettings();

            assertThat(dto.id()).isEqualTo(1L);
            assertThat(dto.dailyWaterConsumption()).isEqualByComparingTo("20");
        }
    }

    @Nested
    @DisplayName("updateSystemSettings")
    class UpdateSystemSettings {

        @Test
        @DisplayName("deve atualizar consumo diário, persistir e retornar DTO")
        void deveAtualizarConsumoPersistirERetornarDto() {
            SystemSettings entity = settingsWithId(1L, new BigDecimal("10"));
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.of(entity));
            when(systemSettingsRepository.save(entity)).thenReturn(entity);

            SystemSettingsDTO input = new SystemSettingsDTO(null, new BigDecimal("25.5"));

            SystemSettingsDTO result = systemSettingsService.updateSystemSettings(input);

            assertThat(entity.getDailyWaterConsumption()).isEqualByComparingTo("25.5");
            assertThat(result.dailyWaterConsumption()).isEqualByComparingTo("25.5");
            assertThat(result.id()).isEqualTo(1L);
            verify(systemSettingsRepository).save(entity);
        }

        @Test
        @DisplayName("deve lançar EntityNotFoundException quando não existir para atualização")
        void deveLancarEntityNotFoundExceptionQuandoNaoExistirParaAtualizacao() {
            when(systemSettingsRepository.findById(1L)).thenReturn(Optional.empty());
            SystemSettingsDTO input = new SystemSettingsDTO(null, new BigDecimal("15"));

            assertThatThrownBy(() -> systemSettingsService.updateSystemSettings(input))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessageContaining("Configurações não encontradas");

            verify(systemSettingsRepository).findById(1L);
            verify(systemSettingsRepository, never()).save(any());
        }
    }

    private static SystemSettings settingsWithId(Long id, BigDecimal daily) {
        SystemSettings settings = new SystemSettings();
        settings.setDailyWaterConsumption(daily);
        setField(settings, "id", id);
        return settings;
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
