package com.example.coolmate.Services;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Models.Product.ProductDetail;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Responses.InventoryResponse;
import com.example.coolmate.Services.Impl.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductDetailRepository productDetailRepository;


    @Override
    public Page<InventoryResponse> getAllInventories(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Inventory> inventoryPage = inventoryRepository.findAll(pageable);

        // Convert Inventory entities to InventoryResponse
        Page<InventoryResponse> inventoryResponsePage = inventoryPage.map(InventoryResponse::fromInventory);

        return inventoryResponsePage;
    }


    public List<Inventory> findByProductDetailId(int productDetailId) {
        return inventoryRepository.findByProductDetail(productDetailId);
    }

    public Page<InventoryResponse> searchInventoriesByVersionName(String versionName, int page, int limit) {
        // Create Pageable object with the provided page and limit
        Pageable pageable = PageRequest.of(page - 1, limit); // page - 1 because Spring Data uses zero-based indexing

        // Fetch ProductDetail entities that match the versionName
        List<ProductDetail> productDetails = productDetailRepository.findByVersionNameContaining(versionName);

        // Extract IDs from ProductDetail entities
        List<Integer> productDetailIds = productDetails.stream()
                .map(ProductDetail::getId)
                .collect(Collectors.toList());

        // Fetch inventories associated with these ProductDetail IDs with pagination
        Page<Inventory> inventoryPage = inventoryRepository.findByProductDetailIdIn(productDetailIds, pageable);

        // Convert Inventories to InventoryResponse
        Page<InventoryResponse> inventoryResponsePage = inventoryPage.map(InventoryResponse::fromInventory);

        return inventoryResponsePage;
    }

    @Override
    public void deleteInventory(int id) throws DataNotFoundException {
        if (!inventoryRepository.existsById(id)) {
            throw new DataNotFoundException("Kho không tồn tại với ID : " + id);
        }
        inventoryRepository.deleteById(id);
    }
}
