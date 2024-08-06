package com.example.coolmate.Models.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_images")
@Entity
public class ProductImage {
    public static final int MAXIMUM_IMAGES_PER_PRODUCT = 6;//chọn nhiều ảnh giới hạn là 5
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore// bỏ qua trường
    private Product product;

    @Column(name = "image_url", length = 250)
    private String imageUrl;
}
