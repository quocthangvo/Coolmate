package com.example.coolmate.Models.Product;

import com.example.coolmate.Models.BaseEntity;
import com.example.coolmate.Models.Category;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    private float price;

    @Column(name = "promotion_price", length = 250)
    private float promotionPrice;

    @Column(name = "image", length = 250)
    private String image;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
