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

    @Column(name = "has_gutter_system", nullable = false)
    private boolean hasGutterSystem;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_family")
    private List<Member> members = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_family")
    private List<WaterDelivery> waterDeliveries = new ArrayList<>();

    public Family() {}

    public Family(
            String name,
            BigDecimal cisternCapacityLiters,
            boolean hasGutterSystem,
            BigDecimal latitude,
            BigDecimal longitude) {
        this.name = name;
        this.cisternCapacityLiters = cisternCapacityLiters;
        this.hasGutterSystem = hasGutterSystem;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
