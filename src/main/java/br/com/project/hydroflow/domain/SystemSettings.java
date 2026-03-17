package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hf_system_settings")
public class SystemSettings {

    @Id
    private Long id;

    @Column(name = "daily_water_consumption", nullable = false)
    private BigDecimal dailyWaterConsumption;

    public SystemSettings() {}

    public Long getId() {
        return id;
    }

    public BigDecimal getDailyWaterConsumption() {
        return dailyWaterConsumption;
    }

    public void setDailyWaterConsumption(BigDecimal dailyWaterConsumption) {
        this.dailyWaterConsumption = dailyWaterConsumption;
    }
}
