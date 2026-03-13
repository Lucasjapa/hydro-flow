package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hf_system_settings")
public class SystemSettings {

    @Id
    @SequenceGenerator(name = "hf_system_settings_id", sequenceName = "hf_system_settings_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_system_settings_id")
    private Long id;

    @Column(name = "daily_water_consumption", nullable = false)
    private BigDecimal dailyWaterConsumption;

    @Column(name = "gutter_efficiency_coefficient", nullable = false)
    private BigDecimal gutterEfficiencyCoefficient;

    public SystemSettings() {}

    public SystemSettings(BigDecimal dailyWaterConsumption, BigDecimal gutterEfficiencyCoefficient) {
        this.dailyWaterConsumption = dailyWaterConsumption;
        this.gutterEfficiencyCoefficient = gutterEfficiencyCoefficient;
    }

    public Long getId() {
        return id;
    }
}
