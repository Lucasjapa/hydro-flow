package br.com.project.hydroflow.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "hf_member")
public class Member {

    @Id
    @SequenceGenerator(name = "hf_member_id", sequenceName = "hf_member_id", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hf_member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private boolean isBedridden;

    public Member() {}

    public Member(String name, int age, boolean isBedridden) {
        this.name = name;
        this.age = age;
        this.isBedridden = isBedridden;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isBedridden() {
        return isBedridden;
    }
}
