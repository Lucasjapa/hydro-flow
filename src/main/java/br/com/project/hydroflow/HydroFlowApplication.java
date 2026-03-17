package br.com.project.hydroflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HydroFlowApplication {

    static void main(String[] args) {
        SpringApplication.run(HydroFlowApplication.class, args);
    }
}
