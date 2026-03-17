package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import br.com.project.hydroflow.repository.SystemSettingsRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
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
    private final SystemSettingsRepository systemSettingsRepository;

    public FamilyService(FamilyRepository familyRepository, SystemSettingsRepository systemSettingsRepository) {
        this.familyRepository = familyRepository;
        this.systemSettingsRepository = systemSettingsRepository;
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
                familyDTO.longitude());

        List<Member> members = familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .toList();

        family.getMembers().addAll(members);

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

        List<Member> members = familyDTO.members().stream()
                .map(memberDTO -> new Member(memberDTO.name(), memberDTO.age(), memberDTO.isBedridden()))
                .toList();

        family.getMembers().addAll(members);

        FamilyDTO familyUpdated = FamilyDTO.from(familyRepository.save(family));
        log.info("Família atualizada com sucesso. id: {}", id);
        return familyUpdated;
    }

    public FamilyDTO findFamilyById(Long id) {
        log.info("Buscando família com id: {}", id);

        return familyRepository.findById(id).map(FamilyDTO::from).orElseThrow(() -> {
            log.warn("Família não encontrada. id: {}", id);
            return new EntityNotFoundException("Família não encontrada: " + id);
        });
    }

    public Page<FamilyDTO> findAllFamilies(Pageable pageable) {
        log.info("Listando famílias. página: {}, tamanho: {}", pageable.getPageNumber(), pageable.getPageSize());

        return familyRepository.findAll(pageable).map(FamilyDTO::from);
    }

    public Page<FamilyDTO> findFamiliesByName(String name, Pageable pageable) {
        log.info("Buscando famílias por nome: '{}'", name);

        return familyRepository.findByNameContainingIgnoreCase(name, pageable).map(FamilyDTO::from);
    }

    public void updateAllCisternLevels() {
        log.info("Iniciando atualização diária do nível da cisterna");

        SystemSettings settings = systemSettingsRepository
                .findById(1L)
                .orElseThrow(() -> new EntityNotFoundException("Configurações do sistema não encontradas"));

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
}
