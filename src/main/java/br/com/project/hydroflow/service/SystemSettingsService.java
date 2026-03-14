package br.com.project.hydroflow.service;

import br.com.project.hydroflow.dto.SystemSettingsDTO;
import br.com.project.hydroflow.repository.SystemSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsService {

    private final SystemSettingsRepository systemSettingsRepository;

    public SystemSettingsService(SystemSettingsRepository systemSettingsRepository) {
        this.systemSettingsRepository = systemSettingsRepository;
    }

    public SystemSettingsDTO findSystemSettings() {
        return systemSettingsRepository
                .findById(1L)
                .map(SystemSettingsDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("Configurações não encontradas"));
    }

    public SystemSettingsDTO updateSystemSettings(SystemSettingsDTO systemSettingsDTO) {
        var systemSettings = systemSettingsRepository
                .findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Configurações não encontradas"));

        systemSettings.setDailyWaterConsumption(systemSettingsDTO.dailyWaterConsumption());
        systemSettings.setGutterEfficiencyCoefficient(systemSettingsDTO.gutterEfficiencyCoefficient());

        return SystemSettingsDTO.from(systemSettingsRepository.save(systemSettings));
    }
}
