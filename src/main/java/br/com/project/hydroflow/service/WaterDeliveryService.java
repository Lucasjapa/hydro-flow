package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.WaterDelivery;
import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.repository.FamilyRepository;
import br.com.project.hydroflow.repository.WaterDeliveryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WaterDeliveryService {

    private static final Logger log = LoggerFactory.getLogger(WaterDeliveryService.class);

    private final WaterDeliveryRepository waterDeliveryRepository;
    private final FamilyRepository familyRepository;

    public WaterDeliveryService(WaterDeliveryRepository waterDeliveryRepository, FamilyRepository familyRepository) {
        this.waterDeliveryRepository = waterDeliveryRepository;
        this.familyRepository = familyRepository;
    }

    public WaterDeliveryDTO saveWaterDelivery(WaterDeliveryDTO waterDeliveryDTO) {
        log.info("Criando entrega de água para família id: {}", waterDeliveryDTO.familyId());

        Family family = familyRepository
                .findById(waterDeliveryDTO.familyId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Família não encontrada. id: " + waterDeliveryDTO.familyId()));

        WaterDelivery saved = waterDeliveryRepository.save(
                new WaterDelivery(waterDeliveryDTO.deliveryDate(), waterDeliveryDTO.waterAmountLiters(), family));

        BigDecimal newLevel = family.getCisternCurrentLevelLiters().add(waterDeliveryDTO.waterAmountLiters());

        family.updateCisternLevel(newLevel);

        WaterDeliveryDTO created = WaterDeliveryDTO.from(saved);
        log.info("Entrega de água criada com sucesso. id: {}", created.id());
        return created;
    }

    public List<WaterDeliveryDTO> findByYear(Integer year) {
        log.info("Buscando entregas de água por ano: {}", year);

        List<WaterDeliveryDTO> deliveries = waterDeliveryRepository.findByYear(year).stream()
                .map(WaterDeliveryDTO::from)
                .toList();

        log.info("Entregas encontradas: {} para o ano: {}", deliveries.size(), year);
        return deliveries;
    }
}
