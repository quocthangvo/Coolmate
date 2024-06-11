package com.example.coolmate.Services;

import com.example.coolmate.Dtos.InventoryDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrder;
import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderStatus;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.PurchaseOrder.PurchaseOrderRepository;
import com.example.coolmate.Services.Impl.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public Inventory createInventory(InventoryDTO inventoryDTO) throws DataNotFoundException {
        // Tìm xem đơn đặt hàng có tồn tại không
        PurchaseOrder purchaseOrder = purchaseOrderRepository
                .findById(inventoryDTO.getPurchaseOrderId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Không tìm thấy đơn đặt hàng id: " + inventoryDTO.getPurchaseOrderId()));

        // Kiểm tra xem đơn đặt hàng có hoạt động không
        if (!purchaseOrder.isActive()) {
            throw new IllegalStateException(
                    "Không thể tạo kho cho đơn hàng không hoạt động.");
        }

        // Kiểm tra xem đơn đặt hàng ở trạng thái "Đã giao" không
        if (!purchaseOrder.getStatus().equals(PurchaseOrderStatus.DELIVERED)) {
            throw new IllegalStateException(
                    "Không thể tạo kho cho đơn hàng không ở trạng thái đã giao.");
        }

        // Kiểm tra xem có kho nào được tạo cho đơn hàng này trước đó không
        List<Inventory> existingInventoryList = inventoryRepository.findByPurchaseOrder(purchaseOrder);

        // Nếu danh sách kho đã tồn tại không rỗng, tức là đã có kho được tạo cho đơn hàng này trước đó
        if (!existingInventoryList.isEmpty()) {
            throw new DataNotFoundException("Kho đã tồn tại cho đơn hàng: " + inventoryDTO.getPurchaseOrderId());
        }

        // Tạo mới kho và lưu vào cơ sở dữ liệu
        Inventory newInventory = Inventory.builder()
                .quantity(inventoryDTO.getQuantity())
                .purchaseOrder(purchaseOrder)
                .build();

        // Lưu vào cơ sở dữ liệu và trả về kho đã tạo
        return inventoryRepository.save(newInventory);
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
