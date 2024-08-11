package com.example.coolmate.Services.Impl;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Responses.InventoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IInventoryService {


    Page<InventoryResponse> getAllInventories(int page, int limit);


    List<Inventory> findByProductDetailId(int productDetailId);

    void deleteInventory(int id) throws DataNotFoundException;
}
