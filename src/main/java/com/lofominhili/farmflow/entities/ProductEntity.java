package com.lofominhili.farmflow.entities;

import com.lofominhili.farmflow.utils.Measure;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@EntityListeners(value = AuditingEntityListener.class)
@Entity
@Table(name = "_product")
@Data
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "measure")
    private Measure measure;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    @CreatedDate
    private LocalDate date;
}
