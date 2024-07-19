package com.example.coolmate.Services;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Repositories.InventoryRepository;
import com.example.coolmate.Repositories.Product.ProductDetailRepository;
import com.example.coolmate.Services.Impl.IInventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final ProductDetailRepository productDetailRepository;


    @Override
    public Page<Inventory> getAllInventories(int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        return inventoryRepository.findAll(pageable);
    }

    public List<Inventory> findByProductDetailId(int productDetailId) {
        return inventoryRepository.findByProductDetail(productDetailId);
    }
}
