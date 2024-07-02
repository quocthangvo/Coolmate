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

    @Column(name = "price_selling", nullable = false, length = 200)
    private float priceSelling; // giá bán

    @Column(name = "promotion_price", length = 200)
    private Float promotionPrice; // giá khuyến mãi

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "product_detail_id", nullable = false)
    private ProductDetail productDetail;

    // Phương thức tiện ích để kiểm tra nếu khuyến mãi đang hoạt động
    public boolean isPromotionActive() {
        LocalDateTime now = LocalDateTime.now();
        return promotionPrice != null && startDate != null && endDate != null &&
                now.isAfter(startDate) && now.isBefore(endDate);
    }

    // Phương thức để lấy giá hiện tại
    public float getCurrentPrice() {
        return isPromotionActive() ? promotionPrice : priceSelling;
    }
}
