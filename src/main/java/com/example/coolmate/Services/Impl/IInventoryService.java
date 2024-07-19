package com.example.coolmate.Services.Impl;

import com.example.coolmate.Models.Inventory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IInventoryService {


    Page<Inventory> getAllInventories(int page, int limit);


    List<Inventory> findByProductDetailId(int productDetailId);
}
