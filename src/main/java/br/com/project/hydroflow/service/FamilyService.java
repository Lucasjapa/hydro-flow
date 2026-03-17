package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.Member;
import br.com.project.hydroflow.dto.FamilyDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    public FamilyDTO saveFamily(FamilyDTO familyDTO) {
        log.info("Criando família: {}", familyDTO.name());

        var family = new Family(
                familyDTO.name(),
                familyDTO.cisternCapacityLiters(),
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
}
