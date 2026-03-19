package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.SystemSettingsDTO;
import br.com.project.hydroflow.repository.SystemSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsService {

    private static final Logger log = LoggerFactory.getLogger(SystemSettingsService.class);

    private final SystemSettingsRepository systemSettingsRepository;

    public SystemSettingsService(SystemSettingsRepository systemSettingsRepository) {
        this.systemSettingsRepository = systemSettingsRepository;
    }

    public SystemSettingsDTO findSystemSettings() {
        log.info("Buscando configurações do sistema");
        return systemSettingsRepository
                .findById(1L)
                .map(settings -> {
                    log.info("Configurações encontradas com sucesso");
                    return SystemSettingsDTO.from(settings);
                })
                .orElseThrow(() -> {
                    log.warn("Configurações do sistema não encontradas");
                    return new EntityNotFoundException("Configurações não encontradas");
                });
    }

    public SystemSettings getSystemSettings() {
        log.info("Buscando configurações do sistema");
        return systemSettingsRepository.findById(1L).orElseThrow(() -> {
            log.warn("Configurações do sistema não encontradas");
            return new EntityNotFoundException("Configurações não encontradas");
        });
    }

    public SystemSettingsDTO updateSystemSettings(SystemSettingsDTO systemSettingsDTO) {
        log.info("Atualizando configurações do sistema");
        var systemSettings = systemSettingsRepository.findById(1L).orElseThrow(() -> {
            log.warn("Configurações do sistema não encontradas para atualização");
            return new EntityNotFoundException("Configurações não encontradas");
        });

        systemSettings.setDailyWaterConsumption(systemSettingsDTO.dailyWaterConsumption());

        SystemSettingsDTO updated = SystemSettingsDTO.from(systemSettingsRepository.save(systemSettings));
        log.info("Configurações do sistema atualizadas com sucesso");
        return updated;
    }
}
