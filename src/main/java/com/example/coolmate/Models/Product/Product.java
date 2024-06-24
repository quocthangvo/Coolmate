package com.example.coolmate.Models.Product;

import com.example.coolmate.Models.BaseEntity;
import com.example.coolmate.Models.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "image", length = 250)
    private String image;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category categoryId;
    
    @OneToMany
    @JoinColumn(name = "product_details")
    private List<ProductDetail> productDetails;

}
