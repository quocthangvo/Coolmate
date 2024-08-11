package com.example.coolmate.Repositories;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.Product.ProductDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Inventory findByProductDetail(ProductDetail productDetail);

    @Query("SELECT i FROM Inventory i WHERE i.productDetail.id = :productDetailId")
    List<Inventory> findByProductDetail(int productDetailId);

    Page<Inventory> findByProductDetailIdIn(List<Integer> productDetailIds, Pageable pageable);
}
