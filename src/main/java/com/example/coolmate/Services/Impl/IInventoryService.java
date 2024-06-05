//package com.example.coolmate.Services.Impl;
//
//
//import com.example.coolmate.Dtos.InventoryDTO;
//import com.example.coolmate.Exceptions.DataNotFoundException;
//import com.example.coolmate.Models.Inventory;
//
//import java.util.List;
//
//public interface IInventoryService {
//    Inventory createInventory(InventoryDTO inventoryDTO) throws DataNotFoundException;
//
//    Inventory getInventoryById(int id);
//
//    List<Inventory> getAllInventories();
//
//    void deleteInventory(int id) throws DataNotFoundException;
//
//    Inventory updateInventory(int inventoryId, InventoryDTO inventoryDTO) throws DataNotFoundException;
//}