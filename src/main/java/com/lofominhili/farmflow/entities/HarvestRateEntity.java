package com.lofominhili.farmflow.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "_harvest_rate")
@Data
public class HarvestRateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private ProductEntity product;

    @Column(name = "amount")
    private Integer amount;
}
