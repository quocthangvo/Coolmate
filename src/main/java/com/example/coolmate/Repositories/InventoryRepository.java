package com.example.coolmate.Repositories;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
