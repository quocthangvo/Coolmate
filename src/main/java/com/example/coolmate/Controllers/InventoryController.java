package com.example.coolmate.Controllers;

import com.example.coolmate.Dtos.InventoryDTO;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryService inventoryService;

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getInventoryById(@PathVariable int id) {
        try {
            Inventory inventory = inventoryService.getInventoryById(id);
            return ResponseEntity.ok(inventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInventory(
            @PathVariable int id,
            @Valid @RequestBody InventoryDTO inventoryDTO) {
        try {
            Inventory updatedInventory = inventoryService.updateInventory(id, inventoryDTO);
            return ApiResponseUtil.successResponse("Supplier updated successfully", updatedInventory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));
        }
    }
}
