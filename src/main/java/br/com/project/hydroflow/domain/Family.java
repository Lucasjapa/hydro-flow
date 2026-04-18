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

    @Column(name = "has_gutter_system", nullable = false)
    private boolean hasGutterSystem;

    @Column(nullable = false)
    private BigDecimal latitude;

    @Column(nullable = false)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "family_status", nullable = false)
    private FamilyStatus familyStatus = FamilyStatus.NORMAL;

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @OneToMany(mappedBy = "family")
    private List<WaterDelivery> waterDeliveries = new ArrayList<>();

    @OneToMany(mappedBy = "family", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cistern> cisterns = new ArrayList<>();

    public enum FamilyStatus {
        NORMAL,
        LOW, // abaixo de 10%
        URGENT // abaixo de 5%
    }

    public Family() {}

    public Family(String name, boolean hasGutterSystem, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.hasGutterSystem = hasGutterSystem;
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

    public boolean isHasGutterSystem() {
        return hasGutterSystem;
    }

    public void setHasGutterSystem(boolean hasGutterSystem) {
        this.hasGutterSystem = hasGutterSystem;
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

    public FamilyStatus getFamilyStatus() {
        return familyStatus;
    }

    public void setFamilyStatus(FamilyStatus familyStatus) {
        this.familyStatus = familyStatus;
    }

    public List<Member> getMembers() {
        return members;
    }

    public List<Cistern> getCisterns() {
        return cisterns;
    }

    public void addMember(Member member) {
        member.setFamily(this);
        this.members.add(member);
    }

    public void addCistern(Cistern cistern) {
        cistern.setFamily(this);
        this.cisterns.add(cistern);
    }

    public void updateStatus(int remainingDays) {
        if (remainingDays <= 5) {
            this.familyStatus = FamilyStatus.URGENT;
        } else if (remainingDays <= 10) {
            this.familyStatus = FamilyStatus.LOW;
        } else {
            this.familyStatus = FamilyStatus.NORMAL;
        }
    }
}
