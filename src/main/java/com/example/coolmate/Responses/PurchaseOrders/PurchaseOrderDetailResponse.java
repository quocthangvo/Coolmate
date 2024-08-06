package com.example.coolmate.Responses.PurchaseOrders;

import com.example.coolmate.Models.PurchaseOrder.PurchaseOrderDetail;
import com.example.coolmate.Responses.Product.ProductDetailResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PurchaseOrderDetailResponse {
    private int id;
    private int quantity;
    private float price;
    private String note;
    private ProductDetailResponse productDetail; // Use ProductDetailResponse to include product details

    // Static method to convert PurchaseOrderDetail entity to PurchaseOrderDetailResponse DTO
    public static PurchaseOrderDetailResponse fromPurchaseOrderDetail(PurchaseOrderDetail detail) {
        return PurchaseOrderDetailResponse.builder()
                .id(detail.getId())
                .quantity(detail.getQuantity())
                .price(detail.getPrice())
                .note(detail.getNote())
                .productDetail(ProductDetailResponse.fromProductDetail(detail.getProductDetail())) // Convert ProductDetail to ProductDetailResponse
                .build();
    }
}
