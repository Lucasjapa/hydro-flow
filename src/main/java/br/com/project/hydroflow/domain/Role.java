package br.com.project.hydroflow.domain;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "hf_role")
public class Role {

    @Id
    @SequenceGenerator(name = "hf_role_id", sequenceName = "hf_role_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_role_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    @JoinTable(
            name = "hf_role_permission",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_permission"))
    private Set<Permission> permissions = new HashSet<>();

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}
