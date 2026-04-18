package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Cistern;
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

        var family =
                new Family(familyDTO.name(), familyDTO.hasGutterSystem(), familyDTO.latitude(), familyDTO.longitude());

        familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .forEach(family::addMember);

        familyDTO.cisterns().stream()
                .map(cisternDTO -> new Cistern(cisternDTO.capacityLiters(), cisternDTO.currentLevelLiters(), family))
                .forEach(family::addCistern);

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
        family.setHasGutterSystem(familyDTO.hasGutterSystem());
        family.setLatitude(familyDTO.latitude());
        family.setLongitude(familyDTO.longitude());

        family.getMembers().clear();
        familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .forEach(family::addMember);

        family.getCisterns().clear();
        familyDTO.cisterns().stream()
                .map(cisternDTO -> new Cistern(cisternDTO.capacityLiters(), cisternDTO.currentLevelLiters(), family))
                .forEach(family::addCistern);

        FamilyDTO familyUpdated = FamilyDTO.from(familyRepository.save(family));
        log.info("Família atualizada com sucesso. id: {}", id);
        return familyUpdated;
    }

    public FamilyDTO findFamilyById(Long id) {
        Family family = getFamilyById(id);

        SystemSettings settings = systemSettingsService.getSystemSettings();

        BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                .multiply(BigDecimal.valueOf(family.getMembers().size()));

        BigDecimal totalWater = family.getCisterns().stream()
                .map(Cistern::getCurrentLevelLiters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int remainingDays = dailyConsumption.compareTo(BigDecimal.ZERO) == 0
                ? Integer.MAX_VALUE
                : totalWater.divide(dailyConsumption, 0, RoundingMode.FLOOR).intValue();

        LocalDate nextDeliveryDate = LocalDate.now().plusDays(remainingDays);

        return FamilyDTO.from(family, dailyConsumption, remainingDays, nextDeliveryDate);
    }

    public Page<FamilyDTO> findAllFamilies(Pageable pageable) {
        log.info("Listando famílias. página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());

        return familyRepository.findAll(pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findFamiliesByName(String name, Pageable pageable) {
        log.info("Buscando famílias por nome: '{}'", name);

        return familyRepository.findByNameContainingIgnoreCase(name, pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findFamiliesByStatus(Family.FamilyStatus status, Pageable pageable) {
        log.info("Buscando famílias por status: {}", status);
        return familyRepository.findByFamilyStatus(status, pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findAllOrderByCisternLevelAsc(Pageable pageable) {
        log.info("Buscando famílias ordenadas por nível da cisterna crescente");
        return familyRepository.findAllOrderByCisternLevelAsc(pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findAllOrderByCisternLevelDesc(Pageable pageable) {
        log.info("Buscando famílias ordenadas por nível da cisterna decrescente");
        return familyRepository.findAllOrderByCisternLevelDesc(pageable).map(FamilyDTO::from);
    }

    @Transactional
    public void updateAllCisternLevels() {
        log.info("Iniciando atualização diária do nível da cisterna");

        SystemSettings settings = systemSettingsService.getSystemSettings();

        List<Family> families = familyRepository.findAll();

        families.forEach(family -> {
            BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                    .multiply(BigDecimal.valueOf(family.getMembers().size()));

            BigDecimal remainingConsumption = dailyConsumption;

            for (Cistern cistern : family.getCisterns()) {
                if (remainingConsumption.compareTo(BigDecimal.ZERO) <= 0) break;

                BigDecimal toConsume = remainingConsumption.min(cistern.getCurrentLevelLiters());
                BigDecimal newLevel = cistern.getCurrentLevelLiters().subtract(toConsume);

                remainingConsumption = remainingConsumption.subtract(toConsume);

                int remainingDays = calculateRemainingDays(cistern, dailyConsumption);

                log.info(
                        "Família id: {} | Cisterna id: {} | Consumido: {}L | Novo nível: {}L | Restante consumo: {}L",
                        family.getId(),
                        cistern.getId(),
                        toConsume,
                        newLevel,
                        remainingConsumption);

                cistern.updateLevel(newLevel, remainingDays);
            }
        });

        familyRepository.saveAll(families);
        log.info("Nível das cisternas atualizado para {} famílias", families.size());
    }

    public int calculateRemainingDays(Cistern cistern, BigDecimal dailyConsumption) {
        if (dailyConsumption.compareTo(BigDecimal.ZERO) == 0) return Integer.MAX_VALUE;

        return cistern.getCurrentLevelLiters()
                .divide(dailyConsumption, 0, RoundingMode.FLOOR)
                .intValue();
    }
}
