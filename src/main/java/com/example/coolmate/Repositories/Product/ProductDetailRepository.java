package com.example.coolmate.Repositories.Product;


import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    Optional<ProductDetail> findById(int id);
    ProductDetail findByProductIdAndSizeId(int productId, int sizeId);
    ProductDetail findByProductIdAndSizeIdAndColorId(int productId, int sizeId, int colorId);
}
