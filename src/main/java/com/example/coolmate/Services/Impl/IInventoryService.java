package com.example.coolmate.Services.Impl;

import com.example.coolmate.Dtos.InventoryDTO;
import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Models.Inventory;

import java.util.List;

public interface IInventoryService {


    Inventory createInventory(InventoryDTO inventoryDTO) throws DataNotFoundException;

    Inventory getInventoryById(int id);

    Inventory updateInventory(int Id, InventoryDTO inventoryDTO) throws DataNotFoundException;

    List<Inventory> getAllInventories(int page, int limit);
}
