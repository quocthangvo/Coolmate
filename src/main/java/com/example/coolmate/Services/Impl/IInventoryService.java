package com.example.coolmate.Services.Impl;

import com.example.coolmate.Models.Inventory;

import java.util.List;

public interface IInventoryService {


    List<Inventory> getAllInventories(int page, int limit);


    List<Inventory> findByProductDetailId(int productDetailId);
}
