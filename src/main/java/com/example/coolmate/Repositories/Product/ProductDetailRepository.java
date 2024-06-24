package com.example.coolmate.Repositories.Product;


import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    Optional<ProductDetail> findById(int id);

    List<ProductDetail> findProductDetailsByProductId(@Param("productId") int productId);

//    ProductDetail findByProductIdAndSizeId(int productId, int sizeId);
//
//    ProductDetail findByProductIdAndSizeIdAndColorId(int productId, int sizeId, int colorId);
//
//    List<ProductDetail> findByColorName(String colorName);
//
//    List<ProductDetail> findBySizeName(String sizeName);


}
