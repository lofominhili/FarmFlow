package com.lofominhili.farmflow.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(value = AuditingEntityListener.class)
@Entity
@Table(name = "_record")
@Data
public class RecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private UserEntity user;

    @ManyToOne
    private ProductEntity product;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    @CreatedDate
    private LocalDate date;
}
