package com.example.coolmate.Services;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Services.Impl.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductDetailRepository productDetailRepository;


    @Override
    public List<Inventory> getAllInventories(int page, int limit) {
        return inventoryRepository.findAll();
    }

    public List<Inventory> findByProductDetailId(int productDetailId) {
        return inventoryRepository.findByProductDetail(productDetailId);
    }
}
