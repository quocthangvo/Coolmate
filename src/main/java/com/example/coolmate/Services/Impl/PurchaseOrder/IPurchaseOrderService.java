package com.example.coolmate.Services.Impl.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IPurchaseOrderService {
    PurchaseOrder createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws Exception;

    PurchaseOrder getPurchaseOrderById(int id) throws DataNotFoundException;

    Page<PurchaseOrder> getAllPurchaseOrders(int page, int limit);


    void deletePurchaseOrder(int id) throws DataNotFoundException;

    PurchaseOrder updatePurchaseOrder(int purchaseOrderId, PurchaseOrderDTO purchaseOrderDTO) throws DataNotFoundException;

    List<PurchaseOrder> searchPurchaseOrderByCode(String code);

    List<PurchaseOrder> getOrderDate(LocalDate orderDate);
}
