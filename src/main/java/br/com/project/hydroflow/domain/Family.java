package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hf_family")
public class Family {

    @Id
    @SequenceGenerator(name = "hf_family_id", sequenceName = "hf_family_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_family_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "cistern_capacity_liters", nullable = false)
    private BigDecimal cisternCapacityLiters;

    @Column(name = "cistern_current_level_liters", nullable = false)
    private BigDecimal cisternCurrentLevelLiters;

    @Column(name = "has_gutter_system", nullable = false)
    private boolean hasGutterSystem;

    @Column(name = "gutter_area_m2")
    private BigDecimal gutterAreaM2;

    @Column(name = "gutter_efficiency_coefficient")
    private BigDecimal gutterEfficiencyCoefficient;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_family")
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "family")
    private List<WaterDelivery> waterDeliveries = new ArrayList<>();

    public Family() {}

    public Family(
            String name,
            BigDecimal cisternCapacityLiters,
            BigDecimal cisternCurrentLevelLiters,
            boolean hasGutterSystem,
            BigDecimal gutterAreaM2,
            BigDecimal gutterEfficiencyCoefficient,
            BigDecimal latitude,
            BigDecimal longitude) {
        this.name = name;
        this.cisternCapacityLiters = cisternCapacityLiters;
        this.cisternCurrentLevelLiters = cisternCurrentLevelLiters;
        this.hasGutterSystem = hasGutterSystem;
        this.gutterAreaM2 = gutterAreaM2;
        this.gutterEfficiencyCoefficient = gutterEfficiencyCoefficient;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCisternCapacityLiters() {
        return cisternCapacityLiters;
    }

    public void setCisternCapacityLiters(BigDecimal cisternCapacityLiters) {
        this.cisternCapacityLiters = cisternCapacityLiters;
    }

    public BigDecimal getCisternCurrentLevelLiters() {
        return cisternCurrentLevelLiters;
    }

    public void setCisternCurrentLevelLiters(BigDecimal cisternCurrentLevelLiters) {
        this.cisternCurrentLevelLiters = cisternCurrentLevelLiters;
    }

    public boolean isHasGutterSystem() {
        return hasGutterSystem;
    }

    public void setHasGutterSystem(boolean hasGutterSystem) {
        this.hasGutterSystem = hasGutterSystem;
    }

    public BigDecimal getGutterAreaM2() {
        return gutterAreaM2;
    }

    public void setGutterAreaM2(BigDecimal gutterAreaM2) {
        this.gutterAreaM2 = gutterAreaM2;
    }

    public BigDecimal getGutterEfficiencyCoefficient() {
        return gutterEfficiencyCoefficient;
    }

    public void setGutterEfficiencyCoefficient(BigDecimal gutterEfficiencyCoefficient) {
        this.gutterEfficiencyCoefficient = gutterEfficiencyCoefficient;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public List<WaterDelivery> getWaterDeliveries() {
        return waterDeliveries;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void updateCisternLevel(BigDecimal newLevel) {
        if (newLevel.compareTo(this.cisternCapacityLiters) > 0) {
            throw new IllegalArgumentException("O nível atual da cisterna não pode ser maior que a capacidade total");
        }
        this.cisternCurrentLevelLiters = newLevel;
    }
}
