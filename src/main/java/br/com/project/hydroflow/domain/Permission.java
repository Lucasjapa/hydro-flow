package br.com.project.hydroflow.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "hf_permission")
public class Permission {

    @Id
    @SequenceGenerator(name = "hf_permission_id", sequenceName = "hf_permission_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_permission_id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    public Permission() {}

    public String getName() {
        return name;
    }
}
