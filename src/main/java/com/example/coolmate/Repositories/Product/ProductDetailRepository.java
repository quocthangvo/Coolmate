package com.example.coolmate.Repositories.Product;


import com.example.coolmate.Models.Product.Color;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Models.Product.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    Optional<ProductDetail> findById(int id);

    List<ProductDetail> findProductDetailsByProductId(@Param("productId") int productId);

    @Query("SELECT pd FROM ProductDetail pd JOIN pd.product p WHERE p.name LIKE %:name%")
    List<ProductDetail> findByNameContaining(@Param("name") String name);

    List<ProductDetail> findBySizeId(int size);

    List<ProductDetail> findByColorId(int color);

    List<ProductDetail> findBySizeAndColor(Size size, Color color);

//    ProductDetail findByProductIdAndSizeId(int productId, int sizeId);
//
//    ProductDetail findByProductIdAndSizeIdAndColorId(int productId, int sizeId, int colorId);
//
//    List<ProductDetail> findByColorName(String colorName);
//
//    List<ProductDetail> findBySizeName(String sizeName);


}
