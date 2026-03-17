package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hf_water_delivery")
public class WaterDelivery {

    @Id
    @SequenceGenerator(name = "hf_water_delivery_id", sequenceName = "hf_water_delivery_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_water_delivery_id")
    private Long id;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(name = "water_amount_liters", nullable = false)
    private BigDecimal waterAmountLiters;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_family", nullable = false)
    private Family family;

    public WaterDelivery() {}

    public WaterDelivery(LocalDate deliveryDate, BigDecimal waterAmountLiters, Family family) {
        this.deliveryDate = deliveryDate;
        this.waterAmountLiters = waterAmountLiters;
        this.family = family;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public BigDecimal getWaterAmountLiters() {
        return waterAmountLiters;
    }

    public Family getFamily() {
        return family;
    }
}
