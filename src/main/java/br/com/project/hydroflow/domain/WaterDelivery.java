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

    public WaterDelivery() {}

    public WaterDelivery(LocalDate deliveryDate, BigDecimal waterAmountLiters) {
        this.deliveryDate = deliveryDate;
        this.waterAmountLiters = waterAmountLiters;
    }

    public Long getId() {
        return id;
    }
}
