package com.example.coolmate.Models.Product;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "prices")
@Entity
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "price", nullable = false, length = 200)
    private Float price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
