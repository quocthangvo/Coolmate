package com.example.coolmate.Repositories;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProductDetail(ProductDetail productDetail);
}
