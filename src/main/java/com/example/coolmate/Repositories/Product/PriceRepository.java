package com.example.coolmate.Repositories.Product;


import com.example.coolmate.Models.Product.Price;
import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PriceRepository extends JpaRepository<Price, Integer> {

    List<Price> findByProductDetail(ProductDetail productDetail);

    Page<Price> findByProductDetailVersionNameContainingIgnoreCase(String versionName, Pageable pageable);
}
