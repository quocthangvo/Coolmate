package com.example.coolmate.Controllers;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Responses.ApiResponses.ApiResponse;
import com.example.coolmate.Responses.ApiResponses.ApiResponseUtil;
import com.example.coolmate.Services.InventoryService;
import com.example.coolmate.Services.Product.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")

public class InventoryController {
    private final InventoryService inventoryService;
    private final ProductDetailService productDetailService;


    @GetMapping("")
    public ResponseEntity<ApiResponse<List<Inventory>>> getAllInventories(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        List<Inventory> inventories = inventoryService.getAllInventories(page, limit);
        return ApiResponseUtil.successResponse("Successfully", inventories);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<Inventory>>> searchByProductDetailId(@RequestParam("productDetailId") int productDetailId) {
        List<Inventory> results = inventoryService.findByProductDetailId(productDetailId);
        return ApiResponseUtil.successResponse("Successfully", results);

    }
}
