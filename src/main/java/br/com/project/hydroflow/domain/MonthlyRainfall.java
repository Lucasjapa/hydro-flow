package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "hf_monthly_rainfall")
public class MonthlyRainfall {

    @Id
    @SequenceGenerator(name = "hf_monthly_rainfall_id", sequenceName = "hf_monthly_rainfall_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_monthly_rainfall_id")
    private Long id;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int month;

    @Column(name = "rainfall_mm", nullable = false)
    private BigDecimal rainfallMM;

    public MonthlyRainfall() {}

    public MonthlyRainfall(int year, int month, BigDecimal rainfallMM) {
        this.year = year;
        this.month = month;
        this.rainfallMM = rainfallMM;
    }

    public Long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public BigDecimal getRainfallMM() {
        return rainfallMM;
    }

    public void setRainfallMM(BigDecimal rainfallMM) {
        this.rainfallMM = rainfallMM;
    }
}
