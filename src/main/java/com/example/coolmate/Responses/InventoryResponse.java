package com.example.coolmate.Responses;

import com.example.coolmate.Models.Inventory;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class InventoryResponse {
    private int id;
    private int quantity;
    private float price;
    private int inventoryQuantity;
    private ProductDetailResponse productDetail; // Use ProductDetailResponse if you want to include details about the product

    public static InventoryResponse fromInventory(Inventory inventory) {
        return InventoryResponse.builder()
                .id(inventory.getId())
                .quantity(inventory.getQuantity())
                .price(inventory.getPrice())
                .inventoryQuantity(inventory.getInventoryQuantity())
                .productDetail(ProductDetailResponse.fromProductDetail(inventory.getProductDetail()))
                .build();
    }

}
