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

    public MonthlyRainfallService(MonthlyRainfallRepository monthlyRainfallRepository) {
        this.monthlyRainfallRepository = monthlyRainfallRepository;
    }

    public MonthlyRainfallDTO saveMonthlyRainfall(MonthlyRainfallDTO dto) {
        log.info("Criando registro de chuva mensal: ano={}, mês={}", dto.year(), dto.month());

        MonthlyRainfall monthlyRainfall =
                monthlyRainfallRepository.save(new MonthlyRainfall(dto.year(), dto.month(), dto.rainfallMM()));

        MonthlyRainfallDTO monthlyRainfallCreated = MonthlyRainfallDTO.from(monthlyRainfall);
        log.info("Registro de chuva mensal criado com sucesso. id: {}", monthlyRainfallCreated.id());
        return monthlyRainfallCreated;
    }

    public MonthlyRainfallDTO updateMonthlyRainfall(Long id, MonthlyRainfallDTO dto) {
        log.info("Atualizando registro de chuva mensal. id: {}", id);

        MonthlyRainfall monthlyRainfall = monthlyRainfallRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro de chuva mensal não encontrado. id: " + id));

        monthlyRainfall.setYear(dto.year());
        monthlyRainfall.setMonth(dto.month());
        monthlyRainfall.setRainfallMM(dto.rainfallMM());

        MonthlyRainfallDTO updated = MonthlyRainfallDTO.from(monthlyRainfallRepository.save(monthlyRainfall));
        log.info("Registro de chuva mensal atualizado com sucesso. id: {}", updated.id());
        return updated;
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
