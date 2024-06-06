package com.example.coolmate.Models.Product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private float price; // giá gốc

    @Column(name = "price_selling",nullable = false,length = 200)
    private float priceSelling; // giá bán

    @Column(name = "promotion_price",nullable = false,length = 200)
    private float promotionPrice; // giá khuyến mãi

    @Column(name = "start_date",nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date",nullable = false)
    private  LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "product_detail_id",nullable = false)
    private ProductDetail productDetail;
}
