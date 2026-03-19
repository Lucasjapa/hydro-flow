package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.MonthlyRainfall;
import br.com.project.hydroflow.dto.MonthlyRainfallDTO;
import br.com.project.hydroflow.repository.MonthlyRainfallRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MonthlyRainfallService {

    private static final Logger log = LoggerFactory.getLogger(MonthlyRainfallService.class);

    private final MonthlyRainfallRepository monthlyRainfallRepository;
    private final FamilyService familyService;

    public MonthlyRainfallService(MonthlyRainfallRepository monthlyRainfallRepository, FamilyService familyService) {
        this.monthlyRainfallRepository = monthlyRainfallRepository;
        this.familyService = familyService;
    }

    public MonthlyRainfallDTO saveMonthlyRainfall(MonthlyRainfallDTO dto) {
        log.info("Criando registro de chuva mensal: ano={}, mês={}", dto.year(), dto.month());

        MonthlyRainfall monthlyRainfall =
                monthlyRainfallRepository.save(new MonthlyRainfall(dto.year(), dto.month(), dto.rainfallMM()));

        familyService.updateCisternLevelByRainfall(dto.rainfallMM());

        MonthlyRainfallDTO monthlyRainfallCreated = MonthlyRainfallDTO.from(monthlyRainfall);
        log.info("Registro de chuva mensal criado com sucesso. id: {}", monthlyRainfallCreated.id());
        return monthlyRainfallCreated;
    }

    public void deleteMonthlyRainfall(Long id) {
        log.info("Removendo registro de chuva mensal. id: {}", id);

        MonthlyRainfall monthlyRainfall = monthlyRainfallRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de chuva mensal não encontrado. id: " + id));

        monthlyRainfallRepository.delete(monthlyRainfall);
        log.info("Registro de chuva mensal removido com sucesso. id: {}", id);
    }

    public List<MonthlyRainfallDTO> findByYear(Integer year) {
        log.info("Buscando registros de chuva mensal por ano: {}", year);

        List<MonthlyRainfallDTO> rainfalls = monthlyRainfallRepository.findByYear(year).stream()
                .map(MonthlyRainfallDTO::from)
                .toList();

        log.info("Registros encontrados: {} para o ano: {}", rainfalls.size(), year);
        return rainfalls;
    }
}
