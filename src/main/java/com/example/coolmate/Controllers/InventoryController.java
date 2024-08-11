package com.example.coolmate.Controllers;

import com.example.coolmate.Exceptions.DataNotFoundException;
import com.example.coolmate.Exceptions.Message.ErrorMessage;
import com.example.coolmate.Exceptions.Message.SuccessfulMessage;
import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Responses.InventoryResponse;
import com.example.coolmate.Services.InventoryService;
import com.example.coolmate.Services.Product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001"})


public class InventoryController {
    private final InventoryService inventoryService;
    private final ProductDetailService productDetailService;


    @GetMapping("")
    public ResponseEntity<ApiResponse<Page<InventoryResponse>>> getAllInventories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        Page<InventoryResponse> inventories = inventoryService.getAllInventories(page, limit);
        return ApiResponseUtil.successResponse("Successfully", inventories);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Inventory>>> searchByProductDetailId(@RequestParam("productDetailId") int productDetailId) {
        List<Inventory> results = inventoryService.findByProductDetailId(productDetailId);
        return ApiResponseUtil.successResponse("Successfully", results);

    }

    @GetMapping("/search/version_name")
    public ResponseEntity<ApiResponse<Map<String, Object>>> searchInventoriesByVersionName(
            @RequestParam String versionName,
            @RequestParam int page,
            @RequestParam int limit) {
        try {
            Page<InventoryResponse> inventoryPage = inventoryService.searchInventoriesByVersionName(versionName, page, limit);

            Map<String, Object> response = new HashMap<>();
            response.put("content", inventoryPage.getContent());
            response.put("totalPages", inventoryPage.getTotalPages());
            response.put("totalElements", inventoryPage.getTotalElements());

            return ApiResponseUtil.successResponse("Successfully retrieved inventories", response);
        } catch (Exception e) {
            return ApiResponseUtil.errorResponse("An error occurred: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable int id) {
        try {
            inventoryService.deleteInventory(id);
            String message = "Xóa sản phẩm trong kho có ID " + id + " thành công";
            return ResponseEntity.ok(new SuccessfulMessage(message));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage()));

        }
    }
}
