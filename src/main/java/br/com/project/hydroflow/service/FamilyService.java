package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FamilyService {

    private static final Logger log = LoggerFactory.getLogger(FamilyService.class);

    private final FamilyRepository familyRepository;
    private final SystemSettingsService systemSettingsService;

    public FamilyService(FamilyRepository familyRepository, SystemSettingsService systemSettingsService) {
        this.familyRepository = familyRepository;
        this.systemSettingsService = systemSettingsService;
    }

    public void save(Family family) {
        familyRepository.save(family);
    }

    public Family getFamilyById(Long id) {
        log.info("Buscando família com id: {}", id);
        return familyRepository.findById(id).orElseThrow(() -> {
            log.warn("Família não encontrada. id: {}", id);
            return new EntityNotFoundException("Família não encontrada: " + id);
        });
    }

    public FamilyDTO saveFamily(FamilyDTO familyDTO) {
        log.info("Criando família: {}", familyDTO.name());

        var family = new Family(
                familyDTO.name(),
                familyDTO.cisternCapacityLiters(),
                familyDTO.cisternCurrentLevelLiters(),
                familyDTO.hasGutterSystem(),
                familyDTO.gutterAreaM2(),
                familyDTO.gutterEfficiencyCoefficient(),
                familyDTO.latitude(),
                familyDTO.longitude(),
                Family.CisternStatus.NORMAL);

        familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .forEach(family::addMember);

        SystemSettings settings = systemSettingsService.getSystemSettings();
        int remainingDays = calculateRemainingDays(family, settings);
        family.updateCisternLevel(familyDTO.cisternCurrentLevelLiters(), remainingDays);

        FamilyDTO familyCreated = FamilyDTO.from(familyRepository.save(family));
        log.info("Família criada com sucesso. id: {}", familyCreated.id());
        return familyCreated;
    }

    public FamilyDTO updateFamily(Long id, FamilyDTO familyDTO) {
        log.info("Atualizando família com id: {}", id);

        var family = familyRepository.findById(id).orElseThrow(() -> {
            log.warn("Família não encontrada para atualização. id: {}", id);
            return new EntityNotFoundException("Família não encontrada: " + id);
        });

        family.setName(familyDTO.name());
        family.setCisternCapacityLiters(familyDTO.cisternCapacityLiters());
        family.setHasGutterSystem(familyDTO.hasGutterSystem());
        family.setGutterAreaM2(familyDTO.gutterAreaM2());
        family.setGutterEfficiencyCoefficient(familyDTO.gutterEfficiencyCoefficient());
        family.setLatitude(familyDTO.latitude());
        family.setLongitude(familyDTO.longitude());

        family.getMembers().clear();

        familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .forEach(family::addMember);

        FamilyDTO familyUpdated = FamilyDTO.from(familyRepository.save(family));
        log.info("Família atualizada com sucesso. id: {}", id);
        return familyUpdated;
    }

    public FamilyDTO findFamilyById(Long id) {
        log.info("Buscando família com id: {}", id);

        Family family = familyRepository.findById(id).orElseThrow(() -> {
            log.warn("Família não encontrada. id: {}", id);
            return new EntityNotFoundException("Família não encontrada: " + id);
        });

        SystemSettings settings = systemSettingsService.getSystemSettings();

        BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                .multiply(BigDecimal.valueOf(family.getMembers().size()));

        int remainingDays = family.getCisternCurrentLevelLiters()
                .divide(dailyConsumption, 0, RoundingMode.FLOOR)
                .intValue();

        LocalDate nextDeliveryDate = LocalDate.now().plusDays(remainingDays);

        BigDecimal percentage = family.getCisternCurrentLevelLiters()
                .divide(family.getCisternCapacityLiters(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        Family.CisternStatus cisternStatus;
        if (percentage.compareTo(BigDecimal.valueOf(5)) <= 0) {
            cisternStatus = Family.CisternStatus.URGENT;
        } else if (percentage.compareTo(BigDecimal.valueOf(10)) <= 0) {
            cisternStatus = Family.CisternStatus.LOW;
        } else {
            cisternStatus = Family.CisternStatus.NORMAL;
        }

        return FamilyDTO.from(family, dailyConsumption, remainingDays, nextDeliveryDate, cisternStatus);
    }

    public Page<FamilyDTO> findAllFamilies(Pageable pageable) {
        log.info("Listando famílias. página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());

        return familyRepository.findAll(pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findFamiliesByName(String name, Pageable pageable) {
        log.info("Buscando famílias por nome: '{}'", name);

        return familyRepository.findByNameContainingIgnoreCase(name, pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findFamiliesByStatus(Family.CisternStatus status, Pageable pageable) {
        log.info("Buscando famílias por status: {}", status);
        return familyRepository.findByCisternStatus(status, pageable).map(FamilyDTO::from);
    }

    @Transactional
    public void updateAllCisternLevels() {
        log.info("Iniciando atualização diária do nível da cisterna");

        SystemSettings settings = systemSettingsService.getSystemSettings();

        List<Family> families = familyRepository.findAll();

        families.forEach(family -> {
            BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                    .multiply(BigDecimal.valueOf(family.getMembers().size()));

            BigDecimal newLevel = family.getCisternCurrentLevelLiters()
                    .subtract(dailyConsumption)
                    .max(BigDecimal.ZERO);

            log.info(
                    "Família id: {} | Membros: {} | Consumo diário: {} | Novo nível: {}",
                    family.getId(),
                    family.getMembers().size(),
                    dailyConsumption,
                    newLevel);

            family.setCisternCurrentLevelLiters(newLevel);
        });

        familyRepository.saveAll(families);
        log.info("Nível da cisterna atualizado para {} famílias", families.size());
    }

    public void updateCisternLevelByRainfall(BigDecimal rainfallMM) {
        log.info("Atualizando nível da cisterna por chuva: {}mm", rainfallMM);

        SystemSettings settings = systemSettingsService.getSystemSettings();
        List<Family> families = familyRepository.findByHasGutterSystemTrue();

        families.forEach(family -> {
            BigDecimal rainfallMeters = rainfallMM.divide(BigDecimal.valueOf(1000), 10, RoundingMode.HALF_UP);

            BigDecimal volumeLiters = family.getGutterAreaM2()
                    .multiply(family.getGutterEfficiencyCoefficient())
                    .multiply(rainfallMeters)
                    .multiply(BigDecimal.valueOf(1000));

            BigDecimal newLevel = family.getCisternCurrentLevelLiters().add(volumeLiters);

            log.info(
                    "Família id: {} | Chuva: {}mm | Volume captado: {}L | Novo nível: {}L",
                    family.getId(),
                    rainfallMM,
                    volumeLiters,
                    newLevel);

            int remainingDays = calculateRemainingDays(family, settings);
            family.updateCisternLevel(newLevel, remainingDays);
        });

        familyRepository.saveAll(families);
        log.info("Nível da cisterna atualizado para {} famílias com sistema de calhas", families.size());
    }

    public int calculateRemainingDays(Family family, SystemSettings settings) {
        BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                .multiply(BigDecimal.valueOf(family.getMembers().size()));

        if (dailyConsumption.compareTo(BigDecimal.ZERO) == 0) return Integer.MAX_VALUE;

        return family.getCisternCurrentLevelLiters()
                .divide(dailyConsumption, 0, RoundingMode.FLOOR)
                .intValue();
    }
}
