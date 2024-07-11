package com.example.coolmate.Repositories.PurchaseOrder;

import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderDetailRepository extends JpaRepository<PurchaseOrderDetail, Integer> {
    //    List<PurchaseOrderDetail> findPurchaseOrderById(int id);

    List<PurchaseOrderDetail> findByPurchaseOrder(PurchaseOrder purchaseOrder);


}
