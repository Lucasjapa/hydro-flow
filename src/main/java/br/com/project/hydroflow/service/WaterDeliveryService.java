package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Family;
import br.com.project.hydroflow.domain.SystemSettings;
import br.com.project.hydroflow.domain.WaterDelivery;
import br.com.project.hydroflow.dto.WaterDeliveryDTO;
import br.com.project.hydroflow.repository.WaterDeliveryRepository;
import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class WaterDeliveryService {

    private static final Logger log = LoggerFactory.getLogger(WaterDeliveryService.class);

    private final WaterDeliveryRepository waterDeliveryRepository;
    private final FamilyService familyService;
    private final SystemSettingsService systemSettingsService;

    public WaterDeliveryService(
            WaterDeliveryRepository waterDeliveryRepository,
            FamilyService familyService,
            SystemSettingsService systemSettingsService) {
        this.waterDeliveryRepository = waterDeliveryRepository;
        this.familyService = familyService;
        this.systemSettingsService = systemSettingsService;
    }

    public WaterDeliveryDTO saveWaterDelivery(WaterDeliveryDTO waterDeliveryDTO) {
        log.info("Criando entrega de água para família id: {}", waterDeliveryDTO.familyId());

        SystemSettings settings = systemSettingsService.getSystemSettings();

        Family family = familyService.getFamilyById(waterDeliveryDTO.familyId());

        BigDecimal availableSpace = family.getCisternCapacityLiters().subtract(family.getCisternCurrentLevelLiters());

        BigDecimal actualDeliveredAmount =
                waterDeliveryDTO.deliveredAmountLiters().min(availableSpace);

        if (actualDeliveredAmount.compareTo(waterDeliveryDTO.deliveredAmountLiters()) < 0) {
            log.info(
                    "Volume entregue corrigido de {}L para {}L pois ultrapassaria a capacidade da cisterna",
                    waterDeliveryDTO.deliveredAmountLiters(),
                    actualDeliveredAmount);
        }

        BigDecimal newLevel = family.getCisternCurrentLevelLiters().add(actualDeliveredAmount);

        family.updateCisternLevel(newLevel, familyService.calculateRemainingDays(family, settings));
        familyService.save(family);

        WaterDelivery saved = waterDeliveryRepository.save(new WaterDelivery(
                waterDeliveryDTO.deliveryDate(),
                waterDeliveryDTO.requestedAmountLiters(),
                actualDeliveredAmount,
                family));

        WaterDeliveryDTO created = WaterDeliveryDTO.from(saved);
        log.info("Entrega de água criada com sucesso. id: {}", created.id());
        return created;
    }

    public List<WaterDeliveryDTO> findByYearAndFamilyId(Integer year, Long familyId) {
        log.info("Buscando entregas de água por ano: {} e família id: {}", year, familyId);

        List<WaterDeliveryDTO> deliveries = waterDeliveryRepository.findByYearAndFamilyId(year, familyId).stream()
                .map(WaterDeliveryDTO::from)
                .toList();

        log.info("Entregas encontradas: {} para o ano: {} e família id: {}", deliveries.size(), year, familyId);
        return deliveries;
    }
}
