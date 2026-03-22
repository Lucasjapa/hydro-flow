package br.com.project.hydroflow.service;

import br.com.project.hydroflow.domain.Cistern;
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

        BigDecimal dailyConsumption = settings.getDailyWaterConsumption()
                .multiply(BigDecimal.valueOf(family.getMembers().size()));

        BigDecimal remainingWater = waterDeliveryDTO.deliveredAmountLiters();

        for (Cistern cistern : family.getCisterns()) {
            if (remainingWater.compareTo(BigDecimal.ZERO) <= 0) break;

            BigDecimal availableSpace = cistern.getCapacityLiters().subtract(cistern.getCurrentLevelLiters());
            BigDecimal toFill = remainingWater.min(availableSpace);

            BigDecimal newLevel = cistern.getCurrentLevelLiters().add(toFill);
            cistern.updateLevel(newLevel, familyService.calculateRemainingDays(cistern, dailyConsumption));

            log.info("Cisterna id: {} | Adicionado: {}L | Novo nível: {}L", cistern.getId(), toFill, newLevel);

            remainingWater = remainingWater.subtract(toFill);
        }

        log.info("Água restante após distribuição: {}L", remainingWater);

        BigDecimal actualDeliveredAmount =
                waterDeliveryDTO.deliveredAmountLiters().subtract(remainingWater);

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
