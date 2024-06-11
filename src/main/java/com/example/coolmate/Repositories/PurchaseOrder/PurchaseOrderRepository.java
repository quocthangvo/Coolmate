package com.example.coolmate.Repositories.PurchaseOrder;


import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    boolean existsByCode(String code);

    PurchaseOrder findByCode(String code);

    Optional<PurchaseOrder> findByIdAndStatus(int id, String status);
}