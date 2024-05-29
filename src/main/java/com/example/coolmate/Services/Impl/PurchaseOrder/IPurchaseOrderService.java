package com.example.coolmate.Services.Impl.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;

import java.util.List;

public interface IPurchaseOrderService {
    PurchaseOrder createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException;

    PurchaseOrder getPurchaseOrderById(int id);

    List<PurchaseOrder> getAllPurchaseOrders();

    void deletePurchaseOrder(int id) throws DataNotFoundException;

    PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException;
}
