package com.example.coolmate.Repositories.Product;


import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, Integer> {
    Price findByProductDetail(ProductDetail productDetail);
}
