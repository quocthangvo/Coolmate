package com.example.coolmate.Repositories;

import com.example.coolmate.Models.ProductImage;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductImageRepository extends CrudRepository<ProductImage, Integer> {
    List<ProductImage> findByProductId(int productId);
}
