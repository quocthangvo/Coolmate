package com.example.coolmate.Services.Impl.PurchaseOrder;

import com.example.coolmate.Dtos.PurchaseOrderDtos.PurchaseOrderDetailDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;

import java.util.List;

public interface IPurchaseOrderDetailService {
    PurchaseOrderDetail createPurchaseOrderDetail(PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException;

    PurchaseOrderDetail getPurchaseOrderDetailById(int id) throws DataNotFoundException;

    List<PurchaseOrderDetail> findPurchaseOrderById(int id);

    void deletePurchaseOrderDetail(int id) throws DataNotFoundException;

    PurchaseOrderDetail updatePurchaseOrderDetail(int purchaseOrderDetailId, PurchaseOrderDetailDTO purchaseOrderDetailDTO) throws DataNotFoundException;
}
