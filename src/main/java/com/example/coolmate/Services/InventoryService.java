package com.example.coolmate.Services;

import com.example.coolmate.Dtos.InventoryDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderDetailRepository;
import com.example.coolmate.Services.Impl.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderDetailRepository purchaseOrderDetailRepository;

    @Override
    public Inventory createInventory(InventoryDTO inventoryDTO) throws DataNotFoundException {
        return null;
    }

    @Override
    public Inventory getInventoryById(int id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory không tồn tại"));
    }

    @Override
    public Inventory updateInventory(int inventoryId, InventoryDTO inventoryDTO) throws DataNotFoundException {
        Inventory existingInventory = getInventoryById(inventoryId);

        existingInventory.setQuantity(inventoryDTO.getQuantity());
        inventoryRepository.save(existingInventory); // Lưu dữ liệu đã được cập nhật

        return existingInventory;
    }


}
