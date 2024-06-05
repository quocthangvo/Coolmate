//package com.example.coolmate.Services;
//
//import com.example.coolmate.Dtos.InventoryDTO;
//import com.example.coolmate.Exceptions.DataNotFoundException;
//import com.example.coolmate.Models.Inventory;
//import com.example.coolmate.Repositories.InventoryRepository;
//import com.example.coolmate.Repositories.Product.ProductDetailRepository;
//import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
//import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
//import com.example.coolmate.Services.Impl.IInventoryService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class InventoryService implements IInventoryService {
//    private final InventoryRepository inventoryRepository;
//
//    private final ProductDetailRepository productDetailRepository;
//
//    private final PurchaseOrderRepository purchaseOrderRepository;
//
//    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;
//
//
//    @Override
//    public Inventory createInventory(InventoryDTO inventoryDTO) throws DataNotFoundException {
//        return null;
//
//
//    }
//
//
//    @Override
//    public Inventory getInventoryById(int id) {
//        return null;
//    }
//
//    @Override
//    public List<Inventory> getAllInventories() {
//        return List.of();
//    }
//
//    @Override
//    public void deleteInventory(int id) throws DataNotFoundException {
//
//    }
//
//    @Override
//    public Inventory updateInventory(int inventoryId, InventoryDTO inventoryDTO) throws DataNotFoundException {
//        return null;
//    }
//}
