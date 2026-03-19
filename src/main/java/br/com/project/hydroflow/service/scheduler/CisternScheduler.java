package br.com.project.hydroflow.service.scheduler;

import br.com.project.hydroflow.service.FamilyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CisternScheduler {

    private static final Logger log = LoggerFactory.getLogger(CisternScheduler.class);

    private final FamilyService familyService;

    public CisternScheduler(FamilyService familyService) {
        this.familyService = familyService;
    }

    //    	@Scheduled(cron = "*/2 * * * * *") // executa a cada 2 segundos, para testes
    @Scheduled(cron = "0 0 0 * * *")
    public void updateCisternLevel() {
        log.info("Iniciando atualização diária do nível da cisterna");
        familyService.updateAllCisternLevels();
        log.info("Atualização diária do nível da cisterna concluída");
    }
}
