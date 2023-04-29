package com.example.pi.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="dog")
public class Dog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="name")
    private String name;
    @Column(name="weight")
    private String weight;
    @Column(name="height")
    private String height;
    @Column(name="life_span")
    private String life_span;
    @Column(name="image", length = 2000, columnDefinition = "TEXT")
    private String image;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dog_temperament",
            joinColumns = @JoinColumn(name = "dog_id"),
            inverseJoinColumns = @JoinColumn(name = "temperament_id"))
   private Set<Temperament> temperaments = new HashSet<>();
}

