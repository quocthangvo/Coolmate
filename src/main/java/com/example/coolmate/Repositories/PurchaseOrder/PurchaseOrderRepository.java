package com.example.coolmate.Repositories.PurchaseOrder;


import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    boolean existsByCode(String code);

    PurchaseOrder findByCode(String code);

    Optional<PurchaseOrder> findByIdAndStatus(int id, String status);

    @Query("SELECT po FROM PurchaseOrder po WHERE po.code LIKE %:code%")
    List<PurchaseOrder> searchPurchaseOrderByCode(@Param("code") String code);

    @Query("SELECT po FROM PurchaseOrder po WHERE DATE(po.orderDate) = :orderDate")
    List<PurchaseOrder> findByOrderDate(@Param("orderDate") LocalDate orderDate);
}